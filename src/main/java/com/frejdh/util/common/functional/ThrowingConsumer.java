package com.frejdh.util.common.functional;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
	@Override
	default void accept(T parameter) {
		try {
			acceptThrows(parameter);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	void acceptThrows(T parameter) throws Exception;
}
