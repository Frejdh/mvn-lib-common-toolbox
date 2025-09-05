package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;

import java.util.Objects;

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
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
public class Conditionals<T, R> {

	protected final List<ThrowableCondition<T, R>> throwableConditions = new ArrayList<>();
	protected final List<AbstractCondition<T, R>> equalsAndPredicateConditions = new ArrayList<>();
	protected final ThrowingSupplier<T> action;

	/**
	 * Constructor with a different return type.
	 * @param action The supplier function.
	 * @param returnType Only used for compiling purposes. Required for Java in order to understand the return type.
	 */
	protected Conditionals(ThrowingSupplier<T> action, Class<R> returnType) {
		this.action = action;
	}

	/**
	 * Constructor with a return type matching the argument type.
	 * @param action The supplier function.
	 */
	protected Conditionals(ThrowingSupplier<T> action) {
		this(action, null);
	}

	/**
	 * Creates a simple monotype instance based on the supplier function.
	 * @param action The operation to execute. It's allowed to throw exceptions.
	 * @param <T> The argument and return type.
	 * @return An instance of {@link Conditionals}
	 */
	public static <T> Conditionals<T, T> when(ThrowingSupplier<T> action) {
		return new Conditionals<>(action);
	}

	/**
	 * Creates an instance based on the supplier function.
	 * @param action The operation to execute. It's allowed to throw exceptions.
	 * @param <T> The argument type.
	 * @param <R> The return type.
	 * @return An instance of {@link Conditionals}
	 */
	public static <T, R> Conditionals<T, R> when(ThrowingSupplier<T> action, Class<R> returnType) {
		return new Conditionals<>(action, returnType);
	}

	/**
	 * Creates a simple monotype instance based on the supplier function.
	 * @param value The value to apply conditions on.
	 * @param <T> The argument and return type.
	 * @return An instance of {@link Conditionals}
	 */
	public static <T> Conditionals<T, T> when(T value) {
		return new Conditionals<>(() -> value);
	}

	/**
	 * Creates an instance based on the supplier function.
	 * @param value The value to apply conditions on.
	 * @param <T> The argument type.
	 * @param <R> The return type.
	 * @return An instance of {@link Conditionals}
	 */
	public static <T, R> Conditionals<T, R> when(T value, Class<R> returnType) {
		return new Conditionals<>(() -> value, returnType);
	}

	/**
	 * Creates a conditional rule. Applies for all types of exceptions.
	 */
	public ThrowableCondition<T, R> throwsAnyException() {
		ThrowableCondition<T, R> condition = new ThrowableCondition<>(this, Collections.singletonList(Throwable.class));
		throwableConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to any given exception class.
	 */
	public ThrowableCondition<T, R> throwsException(List<Class<? extends Throwable>> throwableClasses) {
		if (throwableClasses == null) {
			return null;
		}
		ThrowableCondition<T, R> condition = new ThrowableCondition<>(this, throwableClasses);
		throwableConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to any given exception class.
	 * Also see {@link #throwsException(List)}.
	 */
	@SafeVarargs
	public final ThrowableCondition<T, R> throwsException(Class<? extends Throwable>... throwableClasses) {
		return throwsException(throwableClasses != null ? Arrays.stream(throwableClasses).collect(Collectors.toList()) : null);
	}

	/**
	 * Creates a conditional rule. Applies to any given value.
	 */
	public EqualsValueCondition<T, R> equalsToAny(List<T> equalsToValues) {
		if (equalsToValues == null) {
			return null;
		}
		EqualsValueCondition<T, R> condition = new EqualsValueCondition<>(this, equalsToValues);
		equalsAndPredicateConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Does a `equals()` check for all elements, and fulfills the condition when any element is equal.
	 * Also see {@link #equalsToAny(List)}.
	 */
	@SafeVarargs
	public final EqualsValueCondition<T, R> equalsToAny(T... equalsToValue) {
		return equalsToAny(equalsToValue != null ? Arrays.stream(equalsToValue).collect(Collectors.toList()) : null);
	}

	/**
	 * Creates a conditional rule for checking if a predicate is fulfilled. Applies to any given {@link Predicate}.
	 * Keep in mind, that {@link Predicate} statements can be stacked in the event that it shall support multiple predicates in one check.
	 */
	public EqualsValueCondition<T, R> equalsTo(T equalsToValue) {
		return equalsToAny(equalsToValue);
	}

	/**
	 * Creates a conditional rule for checking if a predicate is fulfilled. Applies to any given {@link Predicate}.
	 * Keep in mind, that {@link Predicate} statements can be stacked in the event that it shall support multiple predicates in one check.
	 */
	public PredicateCondition<T, R> fulfills(Predicate<T> predicate) {
		if (predicate == null) {
			return null;
		}

		PredicateCondition<T, R> condition = new PredicateCondition<>(this, predicate);
		equalsAndPredicateConditions.add(condition);
		return condition;
	}

	/**
	 * Creates a conditional rule. Applies to any given {@link Predicate}.
	 * Keep in mind, that {@link Predicate} statements can be stacked in the event that it shall support multiple predicates in one check.
	 * @deprecated Please use {@link #fulfills(Predicate)} instead.
	 */
	@Deprecated(since = "2.0.0", forRemoval = true)
	public final PredicateCondition<T, R> equalsToPredicate(Predicate<T> equalsToPredicate) {
		return fulfills(equalsToPredicate);
	}

	/**
	 * Creates a conditional rule. Applies to null values.
	 */
	public EqualsValueCondition<T, R> equalsToNull() {
		return equalsToAny(Collections.singletonList((T) null));
	}

	/**
	 * Creates a conditional rule. Applies to non-null values.
	 */
	public PredicateCondition<T, R> equalsToNotNull() {
		return fulfills(Objects::nonNull);
	}

	/**
	 * Creates a conditional rule. Applies to blank values (null values, or string values that only contains whitespace characters).
	 */
	public final PredicateCondition<T, R> equalsToBlank() {
		return fulfills(value -> {
			if (value instanceof CharSequence strValue) {
				return isBlank(strValue);
			}
			return isNull(value);
		});
	}

	/**
	 * Creates a conditional rule. Applies to non-blank values (null values, or string values that only contains whitespace characters).
	 */
	public final PredicateCondition<T, R> equalsToNotBlank() {
		return fulfills(value -> {
			if (value instanceof CharSequence strValue) {
				return isNotBlank(strValue);
			}
			return nonNull(value);
		});
	}

	/**
	 * Executes the operation based on the configured conditions.
	 */
	public R execute() {
		T value = null;
		try {
			value = action.get();
		} catch (Throwable caughtException) { // Check for ThrowableCondition
			Throwable rootCause = ExceptionUtils.getRootCause(caughtException);
			final Throwable e = (rootCause != null) ? rootCause : caughtException;

			// TODO: Check if the peek is actually doing anything???
			Optional<ThrowableCondition<T, R>> resolvedThrowable = throwableConditions.stream()
					.peek(condition -> condition.throwableClasses = condition.throwableClasses.stream()
									.filter(throwable -> throwable.isInstance(e))
									.collect(Collectors.toList())
					)
					.filter(condition -> !condition.throwableClasses.isEmpty())
					.findFirst();

			if (resolvedThrowable.isPresent()) {
				ThrowableCondition<T, R> condition = resolvedThrowable.get();

				if (condition.hasReturnValue()) {
					return condition.returnValue.get();
				}
				else if (condition.hasThrowableValue()) {
					sneakyThrow(condition.throwableValue.apply(caughtException));
				}
			}
			else {
				sneakyThrow(caughtException);
			}
		}

		// Check for EqualsCondition
		if (equalsAndPredicateConditions.isEmpty()) {
			return attemptCast(value);
		}

		final T finalRetval = value;
		Optional<AbstractCondition<T, R>> matchingCondition = equalsAndPredicateConditions.stream()
				.filter(condition -> isFulfillingEqualsCondition(condition, finalRetval))
				.findFirst();
		return (matchingCondition.isPresent()) ? matchingCondition.get().returnValue.get() : attemptCast(finalRetval);
	}

	@SuppressWarnings("unchecked")
	protected R attemptCast(T value) {
		try {
			return (R) value;
		} catch (ClassCastException e) {
			throw new IllegalStateException("Cannot cast the return type as the parameter value retrieved and the returned element type is mismatching", e);
		}
	}

	protected boolean isFulfillingEqualsCondition(AbstractCondition<T, R> condition, T valueToTest) {
		if (condition instanceof EqualsValueCondition) {
			return ((EqualsValueCondition<T, R>) condition).equalsToAnyValue.stream()
					.anyMatch(conditionRetval -> {
						if (valueToTest == null && conditionRetval == null) {
							return true;
						}
						return conditionRetval != null && conditionRetval.equals(valueToTest);
					});
		}
		else { // Predicate condition
			return ((PredicateCondition<T, R>) condition).predicate.test(valueToTest);
		}
	}

}
