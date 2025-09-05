package com.frejdh.util.common.functional.simple;

import java.util.Objects;

/**
 * Represents an operation that accepts three input arguments and returns no result.
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @see TriConsumer
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {
	default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
		Objects.requireNonNull(after);
		return (parameter1, parameter2, parameter3) -> {
			this.accept(parameter1, parameter2, parameter3);
			after.accept(parameter1, parameter2, parameter3);
		};
	}

	void accept(T parameter1, U parameter2, V parameter3);
}

