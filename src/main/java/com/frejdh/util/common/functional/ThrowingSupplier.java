package com.frejdh.util.common.functional;

import java.util.function.Supplier;

import static com.frejdh.util.common.toolbox.CommonUtils.sneakyThrow;

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

	R getThrows() throws Exception;
}

