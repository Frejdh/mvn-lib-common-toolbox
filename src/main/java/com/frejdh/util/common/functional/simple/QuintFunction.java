package com.frejdh.util.common.functional.simple;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents an operation that accepts five input arguments and produces a result.
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 * @param <C> the type of the third argument to the operation
 * @param <D> the type of the fourth argument to the operation
 * @param <E> the type of the fourth argument to the operation
 * @param <R> the type of the result of the function
 * @see Function
 */
@FunctionalInterface
public interface QuintFunction<A, B, C, D, E, R> {
	default <W> QuintFunction<A, B, C, D, E, W> andThen(Function<? super R, ? extends W> after) {
		Objects.requireNonNull(after);
		return (
				parameter1,
				parameter2,
				parameter3,
				parameter4,
				parameter5
		) ->
				after.apply(this.apply(
						parameter1,
						parameter2,
						parameter3,
						parameter4,
						parameter5
				));
	}

	R apply(A parameter1, B parameter2, C parameter3, D parameter4, E parameter5);
}

