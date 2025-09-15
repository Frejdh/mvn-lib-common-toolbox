package com.frejdh.util.common.invocations;

import java.util.List;

/**
 * An implementation of the {@link AbstractCondition} that's used to for equivalence for different values.
 * @param <T> The value type.
 * @author Kevin Frejdh
 */
public class EqualsValueCondition<T, R> extends AbstractCondition<T, R> {
	protected List<T> equalsToAnyValue;

	public EqualsValueCondition(Conditionals<T, R> parent, List<T> equalsToAnyValue) {
		this.parent = parent;
		this.equalsToAnyValue = equalsToAnyValue;
	}

	public EqualsValueCondition(Conditionals<T, R> parent, T equalsToValue) {
		this(parent, List.of(equalsToValue));
	}

}
