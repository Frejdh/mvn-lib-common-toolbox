package com.frejdh.util.common.invocations;

import java.util.List;

public class ThrowableCondition<T> extends Condition<T> {
	protected List<Class<? extends Throwable>> throwableClasses;

	public ThrowableCondition(ThrowableUtils<T> parent, List<Class<? extends Throwable>> throwableClasses) {
		this.parent = parent;
		this.throwableClasses = throwableClasses;
	}

}
