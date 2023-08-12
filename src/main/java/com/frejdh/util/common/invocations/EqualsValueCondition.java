package com.frejdh.util.common.invocations;

import java.util.List;

public class EqualsValueCondition<T> extends AbstractCondition<T> {
	protected List<T> equalsToValue;

	public EqualsValueCondition(Conditionals<T> parent, List<T> equalsToValue) {
		this.parent = parent;
		this.equalsToValue = equalsToValue;
	}

}
