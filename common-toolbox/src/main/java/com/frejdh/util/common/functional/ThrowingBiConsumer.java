package com.frejdh.util.common.functional;

import org.apiguardian.api.API;

import java.util.function.BiConsumer;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
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
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #accept(Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	void acceptThrows(T parameter1, U parameter2) throws Throwable;
}
