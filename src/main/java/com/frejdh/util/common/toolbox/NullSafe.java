package com.frejdh.util.common.toolbox;

import com.frejdh.util.common.functional.ThrowingSupplier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NullSafe {

	/**
	 * Do an null-safe operation in which normally a NullPointerException can be thrown,
	 * but when some variable wasn't found it should simply return null instead. Inspiration of Kotlin's 'nullableA?.nullableB?.variable' syntax.
	 * @param operation The operation/action to with null-safety.
	 * @return The original operational return value, or null if a NullPointerException was encountered in Java.
	 */
	public static <T> T safe(ThrowingSupplier<T> operation) {
		return ThrowableUtils.when(operation)
				.throwsException(NullPointerException.class)
				.thenReturn(null)
				.execute();
	}

	/**
	 * Do an null-safe operation in which normally a NullPointerException can be thrown,
	 * but when some variable wasn't found it should return another value instead. Inspiration of Kotlin's 'nullableVariable ?: FallbackVariable' elvis syntax.
	 * @param operation The operation/action to with null-safety.
	 * @param defaultValue The value to return in case the operation was null.
	 * @return The original operational return value, or null if a NullPointerException was encountered in Java.
	 */
	public static <T> T elvis(ThrowingSupplier<T> operation, T defaultValue) {
		T retval = ThrowableUtils.when(operation)
				.throwsException(NullPointerException.class)
				.thenReturn(defaultValue)
				.execute();
		return retval != null ? retval : defaultValue;
	}

	/**
	 * The same as {@link #firstNonNull(ThrowingSupplier, List)} except with a supplier defining the fallback value.
	 * @param operation The operation/action to with null-safety.
	 * @param fallbackOperations The second/fallback operation to return in case the first operation was null.
	 * @return The original operational return value, or the fallback operation value, or null.
	 */
	@SafeVarargs
	public static <T> T firstNonNull(ThrowingSupplier<T> operation, ThrowingSupplier<T>... fallbackOperations) {
		List<ThrowingSupplier<T>> fallbackList = (fallbackOperations != null && fallbackOperations.length > 0)
				? new LinkedList<>(Arrays.asList(fallbackOperations))
				: new LinkedList<>();
		return firstNonNull(operation, fallbackList);
	}

	/**
	 * The same as {@link #elvis(ThrowingSupplier, Object)} except with a supplier defining the fallback value.
	 * @param operation The operation/action to with null-safety.
	 * @param fallbackOperations The second/fallback operation to return in case the first operation was null.
	 * @return The original operational return value, or the fallback operation value, or null.
	 */
	public static <T> T firstNonNull(ThrowingSupplier<T> operation, List<ThrowingSupplier<T>> fallbackOperations) {
		if (operation == null) {
			return null;
		}

		return ThrowableUtils.when(operation)
				.throwsException(NullPointerException.class)
				.thenReturn(firstNonNull(fallbackOperations.isEmpty() ? null : fallbackOperations.remove(0), fallbackOperations))
				.execute();
	}

}
