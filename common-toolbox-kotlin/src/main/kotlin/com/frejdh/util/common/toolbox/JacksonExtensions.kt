package com.frejdh.util.common.toolbox

import com.fasterxml.jackson.core.type.TypeReference
import java.io.File


/**
 * Convert `this` instance to a JSON string.
 * @return A JSON string representation.
 * @see JacksonUtils.toJson
 */
fun Any.toJson(): String {
    return JacksonUtils.toJson(this)
}

/**
 * Convert `this` instance to a JSON string.
 * @return A JSON string representation.
 * @see JacksonUtils.toXml
 */
fun Any.toXml(): String {
    return JacksonUtils.toXml(this)
}

/**
 * Convert `this` instance to a YAML string.
 * @return A JSON string representation.
 * @see JacksonUtils.toYaml
 */
fun Any.toYaml(): String {
    return JacksonUtils.toYaml(this)
}

/**
 * Deserialize a JSON formatted [File] to a new instance.
 * @param T The type to deserialize to.
 * @return A new instance or collection.
 */
inline fun <reified T> File.deserializeFromJson(): T {
    return JacksonUtils.deserializeFromJsonFile(this.path, object : TypeReference<T>() {})
}

/**
 * Deserialize an XML formatted [File] to a new instance.
 * @param T The type to deserialize to.
 * @return A new instance or collection.
 */
inline fun <reified T> File.deserializeFromXml(): T {
    return JacksonUtils.deserializeFromXmlFile(this.path, object : TypeReference<T>() {})
}

/**
 * Deserialize a YAML formatted [File] to a new instance.
 * @param T The type to deserialize to.
 * @return A new instance or collection.
 */
inline fun <reified T> File.deserializeFromYaml(): T {
    return JacksonUtils.deserializeFromYamlFile(this.path, object : TypeReference<T>() {})
}

/**
 * Serialize an object to a JSON and save it to the current [File] path.
 * @param T The type to serialize.
 * @return A new instance or collection.
 */
fun <T> File.serializeToJson(value: T) {
    return JacksonUtils.serializeToJsonFile<T>(this.path, value)
}

/**
 * Serialize an object to an XML and save it to the current [File] path.
 * @param T The type to serialize.
 * @return A new instance or collection.
 */
fun <T> File.serializeToXml(value: T) {
    return JacksonUtils.serializeToXmlFile<T>(this.path, value)
}

/**
 * Serialize an object to a YAML and save it to the current [File] path.
 * @param T The type to serialize.
 * @return A new instance or collection.
 */
fun <T> File.serializeToYaml(value: T) {
    return JacksonUtils.serializeToYamlFile<T>(this.path, value)
}