package com.frejdh.util.common.functional;

import org.apiguardian.api.API;

import java.util.function.Function;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link Function} implementation capable of throwing exceptions inside of it.
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

	@Override
	default R apply(T parameter) {
		try {
			return applyThrows(parameter);
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #apply(Object)} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	R applyThrows(T parameter) throws Throwable;

}

