package com.frejdh.util.common.toolbox;

import com.frejdh.util.common.functional.ThrowingConsumer;
import com.frejdh.util.common.functional.ThrowingFunction;
import com.frejdh.util.common.invocations.Operators;
import org.apiguardian.api.API;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Util class for easier usage of reflection.
 * Please know what you're doing when utilizing this class.
 *
 * @author Kevin Frejdh
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class ReflectionUtils {

	/**
	 * Invoke a method for a given instance.
	 *
	 * @param instance Instance to execute the method on
	 * @param methodName Name of the method
	 * @param params Parameters to inserted into the method
	 * @return The return value from the invocation, or `null` if none.
	 */
	public static Object invokeMethod(Object instance, String methodName, Object... params) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException {
		int paramCount = params.length;
		Method method;
		Object requiredObj = null;
		Class<?>[] classArray = new Class<?>[paramCount];
		for (int i = 0; i < paramCount; i++) {
			classArray[i] = params[i].getClass();
		}

		try {
			method = instance.getClass().getDeclaredMethod(methodName, classArray);
			method.setAccessible(true);
			requiredObj = method.invoke(instance, params);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		return requiredObj;
	}

	/**
	 * Replace a non-primitive and static value found inside a class.
	 * This method also works on variables that are final (for JDK 11 or below).
	 *
	 * @param instanceWithVariable The class containing the variable to edit.
	 * @param fieldName The name of the field to edit.
	 * @param newValue The new value to set.
	 * @throws NoSuchFieldException   No field found.
	 * @throws IllegalAccessException Security related exception.
	 */
	@API(status = API.Status.DEPRECATED, since = "12")
	public static void setVariable(Object instanceWithVariable, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
		Field field = setFieldToAccessible(instanceWithVariable.getClass(), fieldName);
		field.set(instanceWithVariable, newValue);
		field.setAccessible(false);
	}

	/**
	 * Replace a non-primitive and static value found inside a class.
	 * This method also works on variables that are final (for JDK 11 or below).
	 *
	 * @param classWithVariable The class containing the variable to edit.
	 * @param fieldName The name of the field to edit.
	 * @param newValue The new value to set.
	 * @throws NoSuchFieldException   No field found.
	 * @throws IllegalAccessException Security related exception.
	 */
	@API(status = API.Status.DEPRECATED, since = "12")
	public static void setStaticVariable(Class<?> classWithVariable, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
		doOperationWithFieldAccessEnabled(
				classWithVariable,
				fieldName,
				(field) -> {
					field.set(null, newValue);
				}
		);
	}

	/**
	 * Get a non-primitive and static value found inside of a class.
	 * This method also works on variables that are final (for JDK 11 or below).
	 *
	 * @param classWithVariable The class containing the variable to fetch.
	 * @param fieldName The name of the field to get.
	 * @throws NoSuchFieldException   No field found.
	 * @throws IllegalAccessException Security related exception.
	 */
	@API(status = API.Status.DEPRECATED, since = "12")
	public static <T> T getVariable(Class<?> classWithVariable, String fieldName, Class<T> castTo) throws NoSuchFieldException, IllegalAccessException {
		return doOperationWithFieldAccessEnabled(
				classWithVariable,
				fieldName,
				(Field field) -> castTo.cast(field.get(null))
		);
	}

	/**
	 * Get a non-primitive and non-static value found inside a class.
	 * This method also works on variables that are final (for JDK 11 or below).
	 *
	 * @param instanceWithVariable The instance containing the variable to fetch.
	 * @param fieldName The name of the field to get.
	 * @throws NoSuchFieldException   No field found.
	 * @throws IllegalAccessException Security related exception.
	 */
	@API(status = API.Status.DEPRECATED, since = "12")
	public static <I, T> T getVariable(I instanceWithVariable, String fieldName, Class<T> castTo) throws NoSuchFieldException, IllegalAccessException {
		return doOperationWithFieldAccessEnabled(
				instanceWithVariable.getClass(),
				fieldName,
				(Field field) -> Operators.safeCall(() -> castTo.cast(field.get(instanceWithVariable)))
		);
	}

	private static void doOperationWithFieldAccessEnabled(Class<?> classWithField, String fieldName, ThrowingConsumer<Field> operation) throws NoSuchFieldException, IllegalAccessException {
		doOperationWithFieldAccessEnabled(classWithField, fieldName, (ThrowingFunction<Field, Void>) (field) -> {
			operation.accept(field);
			return null;
		});
	}

	private static <R> R doOperationWithFieldAccessEnabled(Class<?> classWithField, String fieldName, ThrowingFunction<Field, R> operation) throws NoSuchFieldException, IllegalAccessException {
		Field declaredField = classWithField.getDeclaredField(fieldName);
		boolean fieldWasAccessible = declaredField.isAccessible();
		Field modifiedField = setFieldToAccessible(classWithField, fieldName);
		R retval = operation.apply(modifiedField);

		if (!fieldWasAccessible) {
			declaredField.setAccessible(false);
		}

		return retval;
	}

	/**
	 * Enable accessibility to a field manually.
	 *
	 * @param classWithField Class containing the field.
	 * @param fieldName Name of the field.
	 * @return The field that is now accessible.
	 */
	@API(status = API.Status.DEPRECATED, since = "12")
	public static Field setFieldToAccessible(Class<?> classWithField, String fieldName) throws NoSuchFieldException, IllegalAccessException {
		try {
			ReflectionUtils.IllegalAccessController.disableWarning(false);
			Field field = classWithField.getDeclaredField(fieldName);
			field.setAccessible(true);
			Field modifiers = field.getClass().getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			ReflectionUtils.IllegalAccessController.enableWarning(false);
			return field;
		} catch (NoSuchFieldException e) {
			throw new NoSuchFieldException(classWithField.getCanonicalName() + "$" + fieldName);
		}
	}

	public static Class<?> getInnerClassOrEnum(String outerClassPath, String... nameOfInnerClassOrEnum) {
		StringBuilder path = new StringBuilder(outerClassPath);
		for (String p : nameOfInnerClassOrEnum) {
			path.append("$").append(p);
		}

		try {
			return Class.forName(path.toString());
		} catch (ClassNotFoundException e) {
			System.out.println("The class '" + path + "' couldn't be found.");
			return null;
		}
	}

	public static Class<?> getInnerClassOrEnum(Class<?> outerClass, String... nameOfInnerClassOrEnum) {
		return getInnerClassOrEnum(outerClass.getCanonicalName(), nameOfInnerClassOrEnum);
	}

	/**
	 * Disable the illegal access warnings that may appear during reflection.
	 */
	public static void disableIllegalAccessWarning() {
		IllegalAccessController.disableWarning(true);
	}

	/**
	 * Enable the illegal access warnings that may appear during reflection.
	 */
	public static void enableIllegalAccessWarning() {
		IllegalAccessController.enableWarning(true);
	}


	/**
	 * Class for handling illegal access warnings.
	 */
	protected static class IllegalAccessController {
		protected enum WarningState {
			FORCE_ENABLED, ENABLED, FORCE_DISABLED, DISABLED
		}

		private static WarningState state = WarningState.ENABLED;
		private static Object loggerObj;
		private static Object unsafeObj;
		private static Class<?> loggerClass;
		private static Long offset;
		private static boolean isDisabled;

		/**
		 * If forced has been used, you may reset to a normal state by calling this method.
		 */
		public static void resetForcedState() {
			if (state.equals(WarningState.FORCE_ENABLED))
				state = WarningState.ENABLED;
			else if (state.equals(WarningState.FORCE_DISABLED))
				state = WarningState.DISABLED;
		}

		public static void disableWarning(boolean force) {
			if (force)
				state = WarningState.FORCE_DISABLED;
			else if (!state.equals(WarningState.FORCE_ENABLED))
				state = WarningState.DISABLED;

			if (isDisabled || state.equals(WarningState.FORCE_ENABLED))
				return;

			try {
				// Couldn't import the 'unsafe' package on JDK 11 but the following works (somehow):
				Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
				Field field = unsafeClass.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				Object unsafe = field.get(null);

				Method getObjectVolatile = unsafeClass.getDeclaredMethod("getObjectVolatile", Object.class, long.class);
				Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
				Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

				loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
				Field loggerField = loggerClass.getDeclaredField("logger");
				offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
				unsafeObj = getObjectVolatile.invoke(unsafe, loggerClass, offset);
				putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
				isDisabled = true;
			} catch (Exception ignored) {
			}
		}

		@SuppressWarnings("CallToPrintStackTrace")
		public static void enableWarning(boolean force) {
			if (force) {
				state = WarningState.FORCE_ENABLED;
			}
			else if (!state.equals(WarningState.FORCE_DISABLED)) {
				state = WarningState.ENABLED;
			}

			if (isDisabled && !state.equals(WarningState.FORCE_DISABLED)) {
				try {
					Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
					Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
					Field field = unsafeClass.getDeclaredField("theUnsafe");
					field.setAccessible(true);
					Object unsafe = field.get(null);
					putObjectVolatile.invoke(unsafe, loggerClass, offset, unsafeObj);
					isDisabled = false;
				} catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
