package com.frejdh.util.common.toolbox;

import com.frejdh.util.common.functional.ThrowingSupplier;

public class NullSafe {

	/**
	 * Do an null-safe operation in which normally a NullPointerException can be thrown,
	 * but when some variable wasn't found it should simply return null instead. Inspiration of Kotlin's 'nullableA?.nullableB?.variable' syntax.
	 * @param operation The operation/action to with null-safety.
	 * @return The original operational return value, or null if a NullPointerException was encountered in Java.
	 */
	public static <T> T safe(ThrowingSupplier<T> operation) {
		return elvis(operation, null);
	}

	/**
	 * Do an null-safe operation in which normally a NullPointerException can be thrown,
	 * but when some variable wasn't found it should return another value instead. Inspiration of Kotlin's 'nullableVariable ?: FallbackVariable' elvis syntax.
	 * @param operation The operation/action to with null-safety.
	 * @param defaultValue The value to return in case the operation was null.
	 * @return The original operational return value, or null if a NullPointerException was encountered in Java.
	 */
	public static <T> T elvis(ThrowingSupplier<T> operation, T defaultValue) {
		return ThrowableUtils.when(operation)
				.throwsException(NullPointerException.class)
				.thenReturn(defaultValue)
				.execute();
	}

}
