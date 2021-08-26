package com.frejdh.util.common.functional;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {

	@Override
	default void run() {
		try {
			runThrows();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	void runThrows() throws Exception;
}
