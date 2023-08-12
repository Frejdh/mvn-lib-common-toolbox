package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;

public abstract class AbstractCondition<T> {
	protected Conditionals<T> parent;
	protected boolean hasReturnValue;
	protected ThrowingSupplier<T> returnValue;
	protected Throwable throwableValue;

	/**
	 * Use only this method when constants shall be returned. If method invocations are required in order to get the value,
	 * please use {@link #thenReturn(ThrowingSupplier)} instead.
	 * @param returnValue The value to return if the condition is fulfilled
	 * @return The {@link Conditionals} instance
	 */
	public Conditionals<T> thenReturn(T returnValue) {
		this.hasReturnValue = true;
		this.returnValue = () -> returnValue;
		return parent;
	}

	public Conditionals<T> thenReturn(ThrowingSupplier<T> returnValue) {
		this.hasReturnValue = true;
		this.returnValue = (returnValue != null) ? returnValue : (() -> null);
		return parent;
	}

	public Conditionals<T> thenThrow(Throwable throwable) {
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
