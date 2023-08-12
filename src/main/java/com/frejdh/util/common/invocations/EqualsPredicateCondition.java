package com.frejdh.util.common.invocations;

import java.util.function.Predicate;

public class EqualsPredicateCondition<T> extends AbstractCondition<T> {
	protected Predicate<T> equalsToPredicate;

	public EqualsPredicateCondition(Conditionals<T> parent, Predicate<T> equalsToPredicate) {
		this.parent = parent;
		this.equalsToPredicate = equalsToPredicate;
	}

}
