package com.frejdh.util.common.functional;

import com.frejdh.util.common.functional.simple.TriFunction;
import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link TriFunction} implementation capable of throwing exceptions inside of it.
 * @see TriFunction
 */
@FunctionalInterface
public interface ThrowingTriFunction<T, U, V, R> extends org.apache.commons.lang3.function.TriFunction<T, U, V, R> {

	@Override
	default R apply(T parameter1, U parameter2, V parameter3) {
		try {
			return applyThrows(parameter1, parameter2, parameter3);
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #apply(Object, Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	R applyThrows(T parameter1, U parameter2, V parameter3) throws Throwable;

}

