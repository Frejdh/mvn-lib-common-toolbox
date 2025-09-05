package com.frejdh.util.common.functional;

import com.frejdh.util.common.functional.simple.QuintFunction;
import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link QuintFunction} implementation capable of throwing exceptions inside of it.
 * @see QuintFunction
 */
@FunctionalInterface
public interface ThrowingQuintFunction<A, B, C, D, E, R> extends QuintFunction<A, B, C, D, E, R> {

	@Override
	default R apply(A parameter1, B parameter2, C parameter3, D parameter4, E parameter5) {
		try {
			return applyThrows(parameter1, parameter2, parameter3, parameter4, parameter5);
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #apply(Object, Object, Object, Object, Object)} instead.
	 */
	@API(status = INTERNAL)
	R applyThrows(A parameter1, B parameter2, C parameter3, D parameter4, E parameter5) throws Exception;

}

