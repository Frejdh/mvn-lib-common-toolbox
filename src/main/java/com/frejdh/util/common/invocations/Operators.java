package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Functional class that features other programming language functionalities such as 'Safe Calling/Optional Chaining', 'Elvis', and more.
 * @author Kevin Frejdh
 */
public class Operators {

	/**
	 * Do a null-safe operation in which normally a NullPointerException can be thrown.
	 * When one of the variable aren't found, it simply returns null instead.
	 * Inspiration of Kotlin safe call operator, and Javascript's optional chaining mechanics.
	 * In Kotlin/Javascript, the call could look something like: <br><pre>a?.b?.c</pre><br>
	 * which means to proceed with doing the operations until the end is reached,
	 * or to return null if simply cannot continue due to missing a value ('a' or 'b' is null).
	 * @param operation The operation/action to with null-safety.
	 * @return The original operational return value, or null if a NullPointerException was encountered in Java.
	 */
	public static <T> T safeCall(ThrowingSupplier<T> operation) {
		return Conditionals.when(operation)
				.throwsException(NullPointerException.class)
				.thenReturn((T) null)
				.execute();
	}

	/**
	 * Do a null-safe operation in which normally a NullPointerException can be thrown,
	 * but when some variable wasn't found it should return another value instead. Inspiration of Kotlin's <code>nullableVariable1?.nullableVariable2 ?: fallbackVariable</code> elvis syntax.
	 * @param operation The operation/action to fetch using safe call operators.
	 * @param defaultValue The value to return in case the operation was null.
	 * @return The original operational return value, or null if a NullPointerException was encountered in Java.
	 */
	public static <T> T elvis(ThrowingSupplier<T> operation, T defaultValue) {
		T retval = Conditionals.when(operation)
				.throwsException(NullPointerException.class)
				.thenReturn(defaultValue)
				.execute();
		return retval != null ? retval : defaultValue;
	}


	/**
	 * Returns the first non-null value. For Javascript, the equivalent would be nullish coalescing ('??' usage).
	 * <pre>
	 * // Example of what the method code simulates from Javascript
	 * return someVariable ?? "fallback value that shall be returned if the value of 'someVariable' is null"
	 * </pre>
	 * @param operation The operation/action to with null-safety.
	 * @param fallbackOperations The second/fallback operation to return in case the first operation was null.
	 * @return The original operational return value, or the fallback operation value, or null.
	 */
	public static <T> T firstNonNull(ThrowingSupplier<T> operation, List<ThrowingSupplier<T>> fallbackOperations) {
		if (operation == null) {
			return null;
		}

		return Conditionals.when(operation)
				.throwsException(NullPointerException.class).thenReturn(() -> firstNonNull(removeFirstAndGet(fallbackOperations), fallbackOperations))
				.equalsToNull().thenReturn(() -> firstNonNull(removeFirstAndGet(fallbackOperations), fallbackOperations))
				.execute();
	}

	/**
	 * The same as {@link #firstNonNull(ThrowingSupplier, List)}, except it accepts an array of suppliers instead.
	 * Please refer to {@link #firstNonNull(ThrowingSupplier, List)} for method descriptions.
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

	private static <T> ThrowingSupplier<T> removeFirstAndGet(List<ThrowingSupplier<T>> listToRemoveFirstEntryFrom) {
		return !listToRemoveFirstEntryFrom.isEmpty() ? listToRemoveFirstEntryFrom.remove(0) : null;
	}

}
