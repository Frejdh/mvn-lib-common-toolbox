package com.frejdh.util.common.invocations;

import java.util.List;

/**
 * An implementation of the {@link AbstractCondition} that's used to check if a certain exception was thrown.
 * @param <T> The value type.
 * @author Kevin Frejdh
 */
public class ThrowableCondition<T, R> extends AbstractCondition<T, R> {
	protected List<Class<? extends Throwable>> throwableClasses;

	public ThrowableCondition(Conditionals<T, R> parent, List<Class<? extends Throwable>> throwableClasses) {
		this.parent = parent;
		this.throwableClasses = throwableClasses;
	}

}
