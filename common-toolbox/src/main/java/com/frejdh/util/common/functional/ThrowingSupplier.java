package com.frejdh.util.common.functional;

import org.apiguardian.api.API;

import java.util.function.Supplier;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
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
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #get()} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	R getThrows() throws Throwable;
}

