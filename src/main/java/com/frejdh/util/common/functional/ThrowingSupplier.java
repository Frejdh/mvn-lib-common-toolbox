package com.frejdh.util.common.functional;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<R> extends Supplier<R> {

	@Override
	default R get() {
		try {
			return getThrows();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	R getThrows() throws Exception;
}

