package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingFunction;
import com.frejdh.util.common.functional.ThrowingSupplier;

/**
 * An abstract condition typically used by the {@link Conditionals} class.
 * @param <T> The value type.
 */
public abstract class AbstractCondition<T, R> {
	protected Conditionals<T, R> parent;
	protected boolean hasReturnValue;
	protected ThrowingSupplier<R> returnValue;
	protected ThrowingFunction<Throwable, Throwable> throwableValue;

	/**
	 * Return a value on fulfilled conditions.
	 * It's recommended to only this method when constants are returned. If method invocations are required in order to resolve the value,
	 * please use {@link #thenReturn(ThrowingSupplier)} instead as this will be invoked once needed only.
	 * @param returnValue The value to return if the condition is fulfilled
	 * @return The {@link Conditionals} instance
	 */
	public Conditionals<T, R> thenReturn(R returnValue) {
		this.hasReturnValue = true;
		this.returnValue = () -> returnValue;
		return parent;
	}

	/**
	 * Return a value on fulfilled conditions.
	 * @param returnValue The value to return if the condition is fulfilled
	 * @return The {@link Conditionals} instance
	 */
	public Conditionals<T, R> thenReturn(ThrowingSupplier<R> returnValue) {
		this.hasReturnValue = true;
		this.returnValue = (returnValue != null) ? returnValue : (() -> null);
		return parent;
	}

	/**
	 * Throw a value on fulfilled conditions.
	 * @param throwable The exception to throw.
	 * @return The {@link Conditionals} instance
	 */
	public Conditionals<T, R> thenThrow(ThrowingFunction<Throwable, Throwable> throwable) {
		this.throwableValue = throwable;
		return parent;
	}

	/**
	 * Throw a value on fulfilled conditions.
	 * @param throwable The exception to throw.
	 * @return The {@link Conditionals} instance
	 */
	public Conditionals<T, R> thenThrow(Throwable throwable) {
		this.throwableValue = ignored -> throwable;
		return parent;
	}

	public boolean hasReturnValue() {
		return hasReturnValue;
	}

	public boolean hasThrowableValue() {
		return throwableValue != null;
	}

}
