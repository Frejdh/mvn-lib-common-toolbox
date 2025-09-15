package com.frejdh.util.common.functional;

import java.util.function.BiConsumer;

import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link BiConsumer} implementation capable of throwing exceptions inside of it.
 * @see BiConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {
	@Override
	default void accept(T parameter1, U parameter2) {
		try {
			acceptThrows(parameter1, parameter2);
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #accept(Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	void acceptThrows(T parameter1, U parameter2) throws Exception;
}
