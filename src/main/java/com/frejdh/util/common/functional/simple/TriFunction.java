package com.frejdh.util.common.functional.simple;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents an operation that accepts three input arguments and produces a result.
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @param <R> the type of the result of the function
 * @see Function
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
	default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
		Objects.requireNonNull(after);
		return (parameter1, parameter2, parameter3) -> after.apply(this.apply(parameter1, parameter2, parameter3));
	}

	R apply(T parameter1, U parameter2, V parameter3);
}

