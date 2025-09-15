package com.frejdh.util.common.functional;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;
import static org.apiguardian.api.API.Status.INTERNAL;

import org.apiguardian.api.API;

/**
 * {@link Runnable} implementation capable of throwing exceptions inside of it.
 */
@FunctionalInterface
public interface ThrowingRunnable extends Runnable {

	@Override
	default void run() {
		try {
			runThrows();
		} catch (final Exception e) {
			sneakyThrow(e);
			throw new RuntimeException(e); // Never reached, but required for compilation nevertheless
		}
	}

	/**
	 * Please don't use this directly. Please use {@link #run()} instead.
	 */
	@API(status = INTERNAL)
	void runThrows() throws Exception;
}
