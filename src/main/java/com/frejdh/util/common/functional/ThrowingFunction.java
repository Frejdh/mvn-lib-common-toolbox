package com.frejdh.util.common.functional;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {
	@Override
	default R apply(T parameter) {
		try {
			return applyThrows(parameter);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	R applyThrows(T parameter) throws Exception;
}

