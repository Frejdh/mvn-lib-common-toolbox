package com.frejdh.util.common.toolbox;

import jakarta.annotation.Nonnull;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j2;

/**
 * Generic toolbox class.
 *
 * @author Kevin Frejdh
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
@Log4j2
public class CommonUtils {

	protected CommonUtils() {}

	/**
	 * Checks if the number can be considered numeric. Accepts negative and decimal numbers.
	 *
	 * @param input String to check
	 * @param allowCommaDecimal Whether to allow comma ({@code ,}) decimals or not. Default: {@code true}.
	 * @return A boolean indicating whether the string was numeric or not
	 */
	public static boolean isNumeric(String input, boolean allowCommaDecimal) {
		return isNumericImpl(input, allowCommaDecimal ? ",." : ".");
	}

	/**
	 * Checks if the number can be considered numeric. Accepts negative and decimal numbers.
	 *
	 * @param input String to check
	 * @return A boolean indicating whether the string was numeric or not
	 */
	public static boolean isNumeric(String input) {
		return isNumericImpl(input, ",.");
	}

	private static boolean isNumericImpl(String input, String decimalCharacters) {
		if (input == null) {
			return false;
		}

		return input.matches("-?\\d+([" + decimalCharacters + "]\\d+)?");
	}

	/**
	 * Converts a {@link String} to a {@link BigInteger} instance.
	 * The numeric check is based on the return value of the {@link #isNumeric(String)} method.
	 * Unlike the native {@link BigInteger#BigInteger(String)} constructor,
	 * this implementation allows comma decimal values, integer values, and does not throw exceptions.
	 * @param input The string value to convert.
	 * @return A {@link BigInteger} value or null if the value could not be converted.
	 */
	public static BigInteger toBigInteger(String input) {
		var bigDecimal = toBigDecimal(input);
		return (bigDecimal != null) ? bigDecimal.toBigInteger() : null;
	}

	/**
	 * Converts a {@link String} to a {@link BigDecimal} instance.
	 * The numeric check is based on the return value of the {@link #isNumeric(String)} method.
	 * Unlike the native {@link BigDecimal#BigDecimal(String)} constructor,
	 * this implementation allows comma decimal values, integer values, and does not throw exceptions.
	 * @param input The string value to convert.
	 * @return A {@link BigDecimal} value or null if the value could not be converted.
	 */
	public static BigDecimal toBigDecimal(String input) {
		if (input == null) {
			return null;
		}

		String formattedInput = input.replace(",", ".").replaceAll("^0+", "");

		// Add a decimal if missing, otherwise it won't be accepted by the BigDecimal constructor
		if (!formattedInput.matches(".+\\.\\d+")) {
			formattedInput = formattedInput + ".0";
		}

		try {
			return new BigDecimal(formattedInput);
		} catch (NumberFormatException e) {
			log.warn("Failed to convert number [{}] to a {} instance", input, BigDecimal.class.getCanonicalName(), e);
			return null;
		}
	}

	/**
	 * Converts a {@link String} to a {@link Double} instance.
	 * The numeric check is based on the return value of the {@link #isNumeric(String)} method.
	 * Unlike the native {@link Double#parseDouble(String)} constructor,
	 * this implementation allows comma decimal values and does not throw exceptions.
	 * @param input The string value to convert.
	 * @return A {@link Double} value or null if the value could not be converted.
	 */
	public static Double toDouble(String input) {
		if (input == null) {
			return null;
		}

		try {
			return Double.parseDouble(input.replace(",", ".").replaceAll("^0+", ""));
		} catch (NumberFormatException e) {
			log.warn("Failed to convert number [{}] to a {} instance", input, Double.class.getCanonicalName(), e);
			return null;
		}
	}

	/**
	 * Converts a {@link String} to a {@link Integer} instance.
	 * The numeric check is based on the return value of the {@link #isNumeric(String)} method.
	 * Unlike the native {@link Integer#parseInt(String)} constructor,
	 * this implementation allows comma decimal values and does not throw exceptions.
	 * @param input The string value to convert.
	 * @return A {@link Integer} value or null if the value could not be converted.
	 */
	public static Integer toInteger(String input) {
		var doubleValue = toDouble(input);

		if (doubleValue != null && (doubleValue > Integer.MAX_VALUE || doubleValue < Integer.MIN_VALUE)) {
			log.warn("Failed to convert number [{}] to a {} instance. Reason: Value out of range", input, Integer.class.getCanonicalName());
			return null;
		}

		return (doubleValue != null) ? doubleValue.intValue() : null;
	}

	/**
	 * Converts a {@link String} to a {@link Long} instance.
	 * The numeric check is based on the return value of the {@link #isNumeric(String)} method.
	 * Unlike the native {@link Long#parseLong(String)} constructor,
	 * this implementation allows comma decimal values and does not throw exceptions.
	 * @param input The string value to convert.
	 * @return A {@link Long} value or null if the value could not be converted.
	 */
	public static Long toLong(String input) {
		var doubleValue = toDouble(input);
		return (doubleValue != null) ? doubleValue.longValue() : null;
	}

	/**
	 * Merges two objects together. Only fields with the value {@code null} in the first object are replaced with the corresponding value of the second object.
	 * Fields of the type {@link Collection} will be merged.
	 *
	 * @param first The object with the highest priority. Only null values can be replaced.
	 * @param second The object to potentially replace values in the first object with.
	 * @param <T> Class type of objects
	 * @return A new instance which is the merged result
	 * @throws IllegalAccessException Access denied to field
	 * @throws InvalidClassException  The class of the two objects doesn't match
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> T softMerge(T first, T second) throws IllegalAccessException, InvalidClassException {
		if (!first.getClass().equals(second.getClass()))
			throw new InvalidClassException("The class " + first.getClass().getCanonicalName() + " cannot be merged with the class " + second.getClass().getCanonicalName());

		Field[] fields = first.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);

			// If final, skip!
			if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) // getModifiers() is a bitmask, hence why bitwise comparison is used
				continue;

			Object firstVal = field.get(first);
			Object secVal = field.get(second);

			// TODO: Handle list!
			if (firstVal instanceof List && secVal instanceof List) { // If lists, merge
				((List) firstVal).addAll((List) secVal);
				field.set(first, firstVal);
			} else {
				Object value = (firstVal != null) ? firstVal : secVal;
				field.set(first, value);
			}

			field.setAccessible(false);
		}
		return first;
	}

	/**
	 * Merges two objects together. All non-null values in the 'second' object will replace the values in the 'first' object.
	 *
	 * @param first The original object.
	 * @param second All fields that are not null in this instance will replace a value in the 'first' object.
	 * @param <T> Class type of objects
	 * @return A new instance which is the merged result
	 * @throws IllegalAccessException Access denied to field
	 * @throws InvalidClassException  The class of the two objects doesn't match
	 */
	public static <T> T softMergeInverse(T first, T second) throws IllegalAccessException, InvalidClassException {
		if (!first.getClass().equals(second.getClass()))
			throw new InvalidClassException("The class " + first.getClass().getCanonicalName() + " cannot be merged with the class " + second.getClass().getCanonicalName());

		Field[] fields = first.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);

			// If final, skip!
			if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) // getModifiers() is a bitmask, hence why bitwise comparison is used
				continue;

			Object firstVal = field.get(first);
			Object secVal = field.get(second);

			Object value = (secVal != null) ? secVal : firstVal;
			field.set(first, value);

			field.setAccessible(false);
		}
		return first;
	}

	/**
	 * Merges two objects together. ALL fields are replaced in the 'first' object with the values of the 'second' one (including null).
	 * The 'first' object is therefore pretty much replaced completely by the 'second' object, but with its own reference intact.
	 *
	 * @param first The object to keep the reference to.
	 * @param second The object that replaces all of the values in the first object.
	 * @param <T> Class type of objects
	 * @return A new instance which is the merged result
	 * @throws IllegalAccessException Access denied to field
	 * @throws InstantiationException Couldn't create a new instance of a class. Requires empty constructor
	 * @throws InvalidClassException  The class of the two objects doesn't match
	 */
	@SuppressWarnings("unchecked")
	public static <T> T overrideMerge(T first, T second) throws IllegalAccessException, InstantiationException, InvalidClassException {
		if (!first.getClass().equals(second.getClass()))
			throw new InvalidClassException("The class " + first.getClass().getCanonicalName() + " cannot be merged with the class " + second.getClass().getCanonicalName());

		Field[] fields = first.getClass().getDeclaredFields();
		Object retval = first.getClass().newInstance();
		for (Field field : fields) {
			field.setAccessible(true);

			// If final, skip!
			if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) // getModifiers() is a bitmask, hence why bitwise comparison is used
				continue;

			Object secVal = field.get(second);
			field.set(retval, secVal);
			field.setAccessible(false);
		}
		return (T) retval;
	}

	/**
	 * Removes all duplicates for a given fieldname in a List of objects
	 *
	 * @param list List to delete duplicates in
	 * @param fieldname Fieldname to remove duplicates for
	 * @param <T> Class type of objects
	 * @return A boolean with the result
	 * @throws IllegalAccessException Access denied to field
	 */
	public static <T> List<T> removeListDuplicatesByFieldName(List<T> list, String fieldname) throws IllegalAccessException {
		if (list == null || list.isEmpty())
			return null;

		Field[] fields = list.get(0).getClass().getDeclaredFields();
		List<Integer> indexesToRemove = new ArrayList<>();
		for (int i = 0; i < list.size() - 1; i++) {
			T first = list.get(i);

			for (int j = i + 1; j < list.size(); j++) {
				T second = list.get(j);
				for (Field field : fields) {

					// If final, skip!
					if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) // getModifiers() is a bitmask, hence why bitwise comparison is used
						continue;
					else if (!field.getName().toLowerCase().equals(fieldname.toLowerCase())) // Not duplicate
						continue;
					field.setAccessible(true);

					Object firstVal = field.get(first);
					Object secVal = field.get(second);

					if (firstVal != null && firstVal.equals(secVal))
						indexesToRemove.add(i);
					field.setAccessible(false);
				}
			}
		}

		for (int i = indexesToRemove.size() - 1; i >= 0; i--) {
			list.remove(indexesToRemove.get(i).intValue());
		}

		return list;
	}

	// TODO: Remove? I don't see any use for this?
	/**
	 * Merges two objects together in case they are duplicates. ONLY fields with the value null in the first found object are replaced with the corresponding value of the duplicate one. Uses equals().
	 *
	 * @param list The object with the highest priority. Only null values can be replaced.
	 * @param <T> Class type of objects
	 * @return A new instance which is the merged result
	 * @throws IllegalAccessException Access denied to field
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> List<T> softMergeDuplicates(List<T> list) throws IllegalAccessException {
		if (list == null || list.isEmpty())
			return list;

		Set<T> tempList = new LinkedHashSet<>();
		boolean duplicate;
		for (int i = 0; i < list.size(); i++) {
			T el1 = list.get(i);
			duplicate = false;

			for (int j = i + 1; j < list.size(); j++) {
				T el2 = list.get(j);

				if (el1.equals(el2)) {
					Field[] fields = el1.getClass().getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
						Object firstVal = field.get(el1);
						Object secVal = field.get(el2);

						if (firstVal instanceof List && secVal instanceof List) { // If lists, merge
							((List) firstVal).addAll((List) secVal);
							softMergeDuplicates((List) firstVal);
							field.set(el1, firstVal);
						} else {
							Object value = (firstVal != null) ? firstVal : secVal;
							field.set(el1, value);
						}

						field.setAccessible(false);
					}
					duplicate = true;
				}
			}

			if (!duplicate)
				tempList.add(el1);
		}

		list.clear();
		list.addAll(tempList);
		return list;
	}

	/**
	 * Checks if two objects equals each other based on all fields.
	 *
	 * @param first Object 1 to compare with
	 * @param second Object 2 to compare with
	 * @param <T> Class type of objects
	 * @return A boolean with the result
	 * @throws IllegalAccessException Access denied to field
	 * @throws InvalidClassException  The class of the two objects doesn't match
	 * @deprecated Please use {@link #allFieldsAreEqual(Object, Object)} instead.
	 */
	@Deprecated(since = "2.0.0",  forRemoval = true)
	public static <T> boolean allFieldsEquals(T first, T second) throws IllegalAccessException, InvalidClassException {
		return allFieldsAreEqual(first, second);
	}

	/**
	 * Checks if two objects equals each other based on all fields.
	 *
	 * @param first Object 1 to compare with
	 * @param second Object 2 to compare with
	 * @param <T> Class type of objects
	 * @return A boolean with the result
	 * @throws IllegalAccessException Access denied to field
	 * @throws InvalidClassException  The class of the two objects doesn't match
	 */
	public static <T> boolean allFieldsAreEqual(T first, T second) throws IllegalAccessException, InvalidClassException {
		if (!first.getClass().equals(second.getClass()))
			throw new InvalidClassException("The class " + first.getClass().getCanonicalName() + " cannot be merged with the class " + second.getClass().getCanonicalName());

		Field[] fields = first.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object firstVal = field.get(first);
			Object secVal = field.get(second);

			if (!firstVal.equals(secVal))
				return false;
		}
		return true;
	}

	/**
	 * Recast an object with support of using different classloaders
	 *
	 * @param obj Object to cast
	 * @param <T> Class to be used for casting
	 * @return The object after recast
	 * @throws IOException            If IO related exceptions occur
	 * @throws ClassNotFoundException If the class couldn't be found in the current classloader
	 */
	@SuppressWarnings({"unchecked", "TryFinallyCanBeTryWithResources"})
	public static <T> T recastObject(Object obj) throws IOException, ClassNotFoundException {
		if (obj != null) {
			ByteArrayOutputStream baous = new ByteArrayOutputStream();
			{
				ObjectOutputStream oos = new ObjectOutputStream(baous);
				try {
					oos.writeObject(obj);
				} finally {
					try {
						oos.close();
					} catch (Exception ignored) {
					}
				}
			}

			byte[] bb = baous.toByteArray();
			if (bb.length > 0) {
				ByteArrayInputStream bais = new ByteArrayInputStream(bb);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return (T) ois.readObject();
			}
		}
		return null;
	}

	/**
	 * Get the name of the method that called the method that you're located in
	 *
	 * @param showParenthesis If true concat the parenthesis to the name
	 * @return The method name
	 */
	public static String getCallingMethodName(boolean showParenthesis) {
		try {
			return new Throwable().getStackTrace()[2].getMethodName() + (showParenthesis ? "()" : "");
		} catch (Throwable e) {
			throw new RuntimeException("Undefined method name. How is this possible?", e);
		}
	}

	/**
	 * Get the name of the method that you're currently located in
	 *
	 * @param showParenthesis If true concat the parenthesis to the name
	 * @return The method name
	 */
	public static String getMethodName(boolean showParenthesis) {
		try {
			return new Throwable().getStackTrace()[1].getMethodName() + (showParenthesis ? "()" : "");
		} catch (Throwable e) {
			throw new RuntimeException("Undefined method name. How is this possible?", e);
		}
	}

	/**
	 * Get a stacktrace in the form of a string
	 * @param e The exception to derive the stacktrace string from
	 * @return A stacktrace string
	 */
	public static String stacktraceToString(Throwable e) {
		StringWriter stacktrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stacktrace));
		return stacktrace.toString();
	}

	/**
	 * Get a stacktrace in the form of a string
	 * @return A stacktrace string
	 */
	public static String stacktraceToString() {
		StringWriter stacktrace = new StringWriter();
		new Exception().printStackTrace(new PrintWriter(stacktrace));
		return stacktrace.toString();
	}

	/**
	 * Prepend an element to an array.
	 *
	 * @param array 	Array to prepend the element to
	 * @param element 	The element that should be added
	 * @return A new instance of the array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] prependToArray(T[] array, T element) {
		if (array == null) {
			array = (T[]) new Object[1];
		}

		final int oldLength = array.length;
		final int newLength = oldLength + 1;
		T[] newArray = (T[]) new Object[newLength];
		System.arraycopy(array, 0, newArray, 1, newLength);
		newArray[0] = element;
		return newArray;
	}

	/**
	 * Append an element to an array.
	 *
	 * @param array 	Array to append the element to
	 * @param element 	The element that should be added
	 * @return A new instance of the array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] appendToArray(T[] array, T element) {
		if (array == null) {
			array = (T[]) new Object[1];
		}

		final int oldLength = array.length;
		final int newLength = oldLength + 1;
		array = Arrays.copyOf(array, newLength);
		array[oldLength] = element;
		return array;
	}

	/**
	 * Creates a string based on an array. The internal toString() method is used for each element.
	 * Example output: <i>["element 1", "element 2"]</i>.
	 *
	 * @param array	Array that is to be converted to string.
	 * @param <T>	Object type for the array.
	 * @return A string based on the array elements.
	 */
	public static <T> String arrayToString(T[] array) {
		if (array == null || array.length == 0) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder("[");
		boolean isFirst = true;
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);

			// Check if another element exists
			if (i + 1 != array.length) {
				sb.append(", ");
			}
		}
		return sb.append("]").toString();
	}

	/**
	 * Converts a string to list. Elements are separated by a specific pattern.
	 * @param text Text to separate into lists
	 * @param separator Separator characters as one string. Default: {@code ,}
	 * @return A list
	 */
	public static List<String> stringToList(String text, @Nonnull String separator) {
		if (text == null) {
			return new ArrayList<>();
		}

		return new ArrayList<>(Arrays.asList(text.split("\\s*(" + Pattern.quote(separator) + ")", -1))); // Mutable
	}

	/**
	 * Converts a string to list. Elements separated by comma.
	 * @param text Text to separate into lists
	 * @return A list
	 */
	public static List<String> stringToList(String text) {
		return stringToList(text, ",");
	}

	/**
	 * Sneaky throws an exception with the original type. Doesn't require the calling code to have a try/catch when using this method.
	 * @param cause The exception to throw
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Throwable> void sneakyThrow(Throwable cause) throws E {
		throw (E) cause;
	}

	/**
	 * Sneaky throws an exception. Doesn't require the calling code to have a try/catch when using this method.
	 * @param cause The exception to throw
	 */
	@SuppressWarnings("java:S112")
	public static RuntimeException wrapAsRuntimeException(Throwable cause) {
		return (cause instanceof RuntimeException runtimeException ? runtimeException : new RuntimeException(cause.getMessage(), cause));
	}

}
