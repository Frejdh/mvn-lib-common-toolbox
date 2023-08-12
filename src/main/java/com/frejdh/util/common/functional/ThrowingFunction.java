package com.frejdh.util.common.functional;

import java.util.function.Function;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {
	@Override
	default R apply(T parameter) {
		try {
			return applyThrows(parameter);
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	R applyThrows(T parameter) throws Exception;
}

