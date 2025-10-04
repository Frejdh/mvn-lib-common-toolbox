package com.frejdh.util.common.functional;

import org.apiguardian.api.API;

import java.util.function.BiFunction;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link BiFunction} implementation capable of throwing exceptions inside of it.
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R> {

	@Override
	default R apply(T parameter1, U parameter2) {
		try {
			return applyThrows(parameter1, parameter2);
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #apply(Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	R applyThrows(T parameter1, U parameter2) throws Throwable;

}

