package com.frejdh.util.common.functional;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;

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

	void runThrows() throws Exception;
}
