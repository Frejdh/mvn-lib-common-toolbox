package com.frejdh.util.common.functional;

import java.util.function.Function;

import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
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
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #apply(Object)} instead.
	 */
	@API(status = INTERNAL)
	R applyThrows(T parameter) throws Exception;

}

