package com.frejdh.util.common.invocations

/**
 * Return a new [Conditionals] instance with the return type matching the original type.
 *
 * Usage example:
 * ```kotlin
 * val myString = "2"
 * val myVariable: String? = myString.Conditional().equalsTo("1").thenReturn { "1" }.execute()
 * ```
 * @param T The original type for the object. Recommended to omit.
 */
fun <T : Any> T.asConditional(): Conditionals<T, T> {
    return Conditionals.`when`(this)
}

/**
 * Return a new [Conditionals] instance with another return type.
 *
 * Usage example:
 * ```kotlin
 * val myString = "2"
 * val myVariable: Int? = myString.asTypedConditional<_, Int>().equalsTo("1").thenReturn { 1 }.execute()
 * ```
 * @param T The original type for the object. Recommended to use the [underscore operator](https://kotlinlang.org/docs/generics.html#underscore-operator-for-type-arguments) to infer the type.
 * @param R The new return type to use.
 */
inline fun <T : Any, reified R : Any> T.asTypedConditional(): Conditionals<T, R> {
    return Conditionals.`when`(this, R::class.java)
}
