package com.frejdh.util.common.toolbox;

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

	public T execute() {
		try {
			return action.get();
		} catch (Throwable caughtException) {
			Throwable rootCause = ExceptionUtils.getRootCause(caughtException);
			final Throwable e = rootCause != null ? rootCause : caughtException;

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
					return condition.returnValue;
				}
				else if (condition.hasThrowableValue()) {
					sneakyThrow(condition.throwableValue);
				}
			}

			sneakyThrow(caughtException);
			return null;
		}
	}


	/*
	 * Additional classes
	 */

	public static class ThrowableCondition<T> {
		protected ThrowableUtils<T> parent;
		protected List<Class<? extends Throwable>> throwableClasses;
		protected boolean hasReturnValue;
		protected T returnValue;
		protected Throwable throwableValue;

		public ThrowableCondition(ThrowableUtils<T> parent, List<Class<? extends Throwable>> throwableClasses) {
			this.parent = parent;
			this.throwableClasses = throwableClasses;
		}

		public ThrowableUtils<T> thenReturn(T returnValue) {
			this.hasReturnValue = true;
			this.returnValue = returnValue;
			return parent;
		}

		public ThrowableUtils<T> thenThrow(Throwable throwable) {
			this.throwableValue = throwable;
			return parent;
		}

		public boolean hasReturnValue() {
			return hasReturnValue;
		}

		public boolean hasThrowableValue() {
			return throwableValue != null;
		}
	}

}
