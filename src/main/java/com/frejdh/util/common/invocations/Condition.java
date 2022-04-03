package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;
import com.frejdh.util.common.invocations.ThrowableUtils;

public abstract class Condition<T> {
	protected ThrowableUtils<T> parent;
	protected boolean hasReturnValue;
	protected ThrowingSupplier<T> returnValue;
	protected Throwable throwableValue;

	/**
	 * Use only this method when constant values are returned. If method invocations are required to get the value,
	 * please use {@link #thenReturn(ThrowingSupplier)} to ensure only one invocation!
	 *
	 * @param returnValue
	 * @return
	 */
	public ThrowableUtils<T> thenReturn(T returnValue) {
		this.hasReturnValue = true;
		this.returnValue = () -> returnValue;
		return parent;
	}

	public ThrowableUtils<T> thenReturn(ThrowingSupplier<T> returnValue) {
		this.hasReturnValue = true;
		this.returnValue = (returnValue != null) ? returnValue : (() -> null);
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
