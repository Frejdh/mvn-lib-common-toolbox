package com.frejdh.util.common.invocations;

import java.util.function.Predicate;

/**
 * An implementation of the {@link AbstractCondition} that's used to check if a given predicate is fulfilled.
 * @param <T> The value type used in the predicate.
 * @author Kevin Frejdh
 */
public class PredicateCondition<T, R> extends AbstractCondition<T, R> {
	protected Predicate<T> predicate;

	public PredicateCondition(Conditionals<T, R> parent, Predicate<T> predicate) {
		this.parent = parent;
		this.predicate = predicate;
	}

}
