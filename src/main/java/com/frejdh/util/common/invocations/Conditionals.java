package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Functional class that helps runs a supplier, and acts according to any conditions configured. Example:<br>
 * <pre>
 * Throwables.when(() -> possibleNullPointerExceptionCall()))
 *     .throwsException(NullPointerException.class)
 *     .thenReturn((T) null)
 *     .execute();
 * </pre>
 *
 * @param <T> The return value type
 * @author Kevin Frejdh
 */
public class Conditionals<T> {

	protected final List<ThrowableCondition<T>> throwableConditions = new ArrayList<>();
	protected final List<AbstractCondition<T>> equalsAndPredicateConditions = new ArrayList<>();

	protected final ThrowingSupplier<T> action;

	protected Conditionals(ThrowingSupplier<T> action) {
		this.action = action;
	}

	/**
	 * Creates the configurable class instance based on the operation to run.
	 * @param action The operation to execute that might throw exceptions
	 * @return An instance of {@link Conditionals}
	 */
	public static <T> Conditionals<T> when(ThrowingSupplier<T> action) {
		return new Conditionals<>(action);
	}

	/**
	 * Creates a conditional rule. Applies for all types of exceptions.
	 */
	public ThrowableCondition<T> throwsAnyException() {
		ThrowableCondition<T> condition = new ThrowableCondition<>(this, Collections.singletonList(Throwable.class));
		throwableConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to any given exception class.
	 */
	public ThrowableCondition<T> throwsException(List<Class<? extends Throwable>> throwableClasses) {
		if (throwableClasses == null) {
			return null;
		}
		ThrowableCondition<T> condition = new ThrowableCondition<>(this, throwableClasses);
		throwableConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to any given exception class.
	 * Also see {@link #throwsException(List)}.
	 */
	@SafeVarargs
	public final ThrowableCondition<T> throwsException(Class<? extends Throwable>... throwableClasses) {
		return throwsException(throwableClasses != null ? Arrays.stream(throwableClasses).collect(Collectors.toList()) : null);
	}

	/**
	 * Creates a conditional rule. Applies to any given value.
	 */
	public EqualsValueCondition<T> equalsToAny(List<T> equalsToValues) {
		if (equalsToValues == null) {
			return null;
		}
		EqualsValueCondition<T> condition = new EqualsValueCondition<>(this, equalsToValues);
		equalsAndPredicateConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to any given value.
	 * Also see {@link #equalsToAny(List)}.
	 */
	@SafeVarargs
	public final EqualsValueCondition<T> equalsToAny(T... equalsToValue) {
		return equalsToAny(equalsToValue != null ? Arrays.stream(equalsToValue).collect(Collectors.toList()) : null);
	}

	/**
	 * Creates a conditional rule. Applies to any given {@link Predicate}.
	 * Keep in mind, that {@link Predicate} statements can be stacked in the event that it shall support multiple predicates in one check.
	 */
	public final EqualsPredicateCondition<T> equalsToPredicate(Predicate<T> equalsToPredicate) {
		if (equalsToPredicate == null) {
			return null;
		}

		EqualsPredicateCondition<T> condition = new EqualsPredicateCondition<>(this, equalsToPredicate);
		equalsAndPredicateConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to null values.
	 */
	public final EqualsValueCondition<T> equalsToNull() {
		return equalsToAny(Collections.singletonList((T) null));
	}

	/**
	 * Creates a conditional rule. Applies to blank values (null values, or string values that only contains whitespace characters).
	 */
	public final EqualsPredicateCondition<T> equalsToBlank() {
		return equalsToPredicate((value) -> {
			if (value instanceof String) {
				return isBlank((String) value);
			}
			return isNull(value);
		});
	}

	/**
	 * Executes the operation based on the configured conditions.
	 */
	public T execute() {
		T retval = null;
		try {
			retval = action.get();
		} catch (Throwable caughtException) { // Check for ThrowableCondition
			Throwable rootCause = ExceptionUtils.getRootCause(caughtException);
			final Throwable e = (rootCause != null) ? rootCause : caughtException;

			Optional<ThrowableCondition<T>> matchingCondition = throwableConditions.stream()
					.peek(condition -> condition.throwableClasses = condition.throwableClasses.stream()
									.filter(throwable -> throwable.isInstance(e))
									.collect(Collectors.toList())
					)
					.filter(condition -> !condition.throwableClasses.isEmpty())
					.findFirst();

			if (matchingCondition.isPresent()) {
				ThrowableCondition<T> condition = matchingCondition.get();

				if (condition.hasReturnValue()) {
					retval = condition.returnValue.get();
				}
				else if (condition.hasThrowableValue()) {
					sneakyThrow(condition.throwableValue);
				}
			}
			else {
				sneakyThrow(caughtException);
			}
		}

		// Check for EqualsCondition
		if (equalsAndPredicateConditions.isEmpty()) {
			return retval;
		}

		final T finalRetval = retval;
		Optional<AbstractCondition<T>> matchingCondition = equalsAndPredicateConditions.stream()
				.filter(condition -> isFulfillingEqualsCondition(condition, finalRetval))
				.findFirst();
		return (matchingCondition.isPresent()) ? matchingCondition.get().returnValue.get() : finalRetval;
	}

	protected boolean isFulfillingEqualsCondition(AbstractCondition<T> condition, T valueToTest) {
		if (condition instanceof EqualsValueCondition) {
			return ((EqualsValueCondition<T>) condition).equalsToValue.stream()
					.anyMatch(conditionRetval -> {
						if (valueToTest == null && conditionRetval == null) {
							return true;
						}
						return conditionRetval != null && conditionRetval.equals(valueToTest);
					});
		}
		else { // EqualsPredicateCondition
			return ((EqualsPredicateCondition<T>) condition).equalsToPredicate.test(valueToTest);
		}
	}

}
