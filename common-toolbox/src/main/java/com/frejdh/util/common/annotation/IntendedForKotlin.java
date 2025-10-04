package com.frejdh.util.common.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the function or property as intended for specifically Kotlin usage.
 * The annotated functionality for Java might unstable as the implementation can change depending on how it's referred to in the Kotlin module.
 * Please refer to alternatives provided by the message parameter if other alternatives are preferred for regular Java usage.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface IntendedForKotlin {

	/**
	 * Optional description.
	 */
	String value() default "";

	/**
	 * Should alternative suggestion to use instead for regular Java.
	 */
	String[] alternatives() default {};

}
