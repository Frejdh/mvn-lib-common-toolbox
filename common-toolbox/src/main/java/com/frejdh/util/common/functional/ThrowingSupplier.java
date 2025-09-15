package com.frejdh.util.common.functional;

import java.util.function.Supplier;

import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link Supplier} implementation capable of throwing exceptions inside of it.
 */
@FunctionalInterface
public interface ThrowingSupplier<R> extends Supplier<R> {

	@Override
	default R get() {
		try {
			return getThrows();
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #get()} instead.
	 */
	@API(status = INTERNAL)
	R getThrows() throws Exception;
}

