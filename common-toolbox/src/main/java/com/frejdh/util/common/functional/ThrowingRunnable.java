package com.frejdh.util.common.functional;

import org.apiguardian.api.API;

import static com.frejdh.util.common.toolbox.CommonUtils.wrapAsRuntimeException;
import static org.apiguardian.api.API.Status.INTERNAL;

/**
 * {@link Runnable} implementation capable of throwing exceptions inside of it.
 */
@FunctionalInterface
public interface ThrowingRunnable extends Runnable {

	@Override
	default void run() {
		try {
			runThrows();
		} catch (final Throwable e) {
			throw wrapAsRuntimeException(e);
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #run()} instead.
	 */
	@API(status = INTERNAL)
	@SuppressWarnings("java:S112")
	void runThrows() throws Throwable;
}
