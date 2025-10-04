package com.frejdh.util.common.functional;

import org.apiguardian.api.API;

import java.util.function.Consumer;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
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
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #accept(Object)} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	void acceptThrows(T parameter) throws Throwable;

}
