package com.frejdh.util.common.functional;

import java.util.function.Consumer;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
	@Override
	default void accept(T parameter) {
		try {
			acceptThrows(parameter);
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	void acceptThrows(T parameter) throws Exception;
}
