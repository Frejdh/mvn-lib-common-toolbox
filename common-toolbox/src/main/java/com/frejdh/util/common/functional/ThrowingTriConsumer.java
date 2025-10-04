package com.frejdh.util.common.functional;

import org.apache.commons.lang3.function.TriConsumer;
import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link TriConsumer} implementation capable of throwing exceptions inside of it.
 * @see TriConsumer
 */
@FunctionalInterface
public interface ThrowingTriConsumer<T, U, V> extends TriConsumer<T, U, V> {
	@Override
	default void accept(T parameter1, U parameter2, V parameter3) {
		try {
			acceptThrows(parameter1, parameter2, parameter3);
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #accept(Object, Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	void acceptThrows(T parameter1, U parameter2, V parameter3) throws Throwable;
}
