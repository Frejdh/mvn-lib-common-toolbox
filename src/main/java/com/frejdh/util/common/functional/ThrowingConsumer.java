package com.frejdh.util.common.functional;

import java.util.function.Consumer;

import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link Consumer} implementation capable of throwing exceptions inside of it.
 */
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

	/**
	 * Please don't use this directly. Please use {@link #accept(Object)} instead.
	 */
	@API(status = INTERNAL)
	void acceptThrows(T parameter) throws Exception;

}
