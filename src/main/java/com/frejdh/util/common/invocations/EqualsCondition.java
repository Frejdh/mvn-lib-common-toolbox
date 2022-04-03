package com.frejdh.util.common.invocations;

import java.util.List;

public class EqualsCondition<T> extends Condition<T> {
	protected List<T> equalsToValue;

	public EqualsCondition(ThrowableUtils<T> parent, List<T> equalsToValue) {
		this.parent = parent;
		this.equalsToValue = equalsToValue;
	}

}
