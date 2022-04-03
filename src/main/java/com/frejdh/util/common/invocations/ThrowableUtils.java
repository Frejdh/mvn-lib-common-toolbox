package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;

public class ThrowableUtils<T> {

	private final List<ThrowableCondition<T>> throwableConditions = new ArrayList<>();
	private final List<EqualsCondition<T>> equalsToConditions = new ArrayList<>();
	private final ThrowingSupplier<T> action;

	protected ThrowableUtils(ThrowingSupplier<T> action) {
		this.action = action;
	}

	public static <T> ThrowableUtils<T> when(ThrowingSupplier<T> action) {
		return new ThrowableUtils<>(action);
	}

	public ThrowableCondition<T> throwsAnyException() {
		ThrowableCondition<T> condition = new ThrowableCondition<>(this, Collections.singletonList(Throwable.class));
		throwableConditions.add(condition);
		return condition;
	}

	public ThrowableCondition<T> throwsException(List<Class<? extends Throwable>> throwableClasses) {
		if (throwableClasses == null) {
			return null;
		}
		ThrowableCondition<T> condition = new ThrowableCondition<>(this, throwableClasses);
		throwableConditions.add(condition);
		return condition;
	}

	@SafeVarargs
	public final ThrowableCondition<T> throwsException(Class<? extends Throwable>... throwableClasses) {
		return throwsException(throwableClasses != null ? Arrays.stream(throwableClasses).collect(Collectors.toList()) : null);
	}

	// Lower priority than exceptions
	public EqualsCondition<T> equalsToAny(List<T> equalsToValues) {
		if (equalsToValues == null) {
			return null;
		}
		EqualsCondition<T> condition = new EqualsCondition<>(this, equalsToValues);
		equalsToConditions.add(condition);
		return condition;
	}

	@SafeVarargs
	public final EqualsCondition<T> equalsToAny(T... equalsToValue) {
		return equalsToAny(equalsToValue != null ? Arrays.stream(equalsToValue).collect(Collectors.toList()) : null);
	}

	public final EqualsCondition<T> equalsToNull() {
		return equalsToAny(Collections.singletonList((T) null));
	}

	public T execute() {
		T retval = null;
		try {
			retval = action.get();
		} catch (Throwable caughtException) { // Check for ThrowableCondition
			Throwable rootCause = ExceptionUtils.getRootCause(caughtException);
			final Throwable e = (rootCause != null) ? rootCause : caughtException;

			Optional<ThrowableCondition<T>> matchingCondition = throwableConditions.stream()
					.peek(condition -> condition.throwableClasses = condition.throwableClasses.stream()
									.filter(throwable -> e.getClass().equals(throwable))
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
		if (equalsToConditions.isEmpty()) {
			return retval;
		}

		final T finalRetval = retval;
		Optional<EqualsCondition<T>> matchingCondition = equalsToConditions.stream()
				.peek(condition -> condition.equalsToValue = condition.equalsToValue.stream()
						.filter(conditionRetval -> {
							if (finalRetval == null && conditionRetval == null) {
								return true;
							}
							return conditionRetval != null && conditionRetval.equals(finalRetval);
						})
						.collect(Collectors.toList())
				)
				.filter(condition -> !condition.equalsToValue.isEmpty())
				.findFirst();
		return (matchingCondition.isPresent()) ? matchingCondition.get().returnValue.get() : finalRetval;
	}

}
