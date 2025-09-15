package com.frejdh.util.common.functional;

import java.util.function.BiFunction;

import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
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
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #apply(Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	R applyThrows(T parameter1, U parameter2) throws Exception;

}

