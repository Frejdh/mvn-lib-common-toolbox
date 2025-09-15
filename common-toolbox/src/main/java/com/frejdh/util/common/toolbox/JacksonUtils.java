package com.frejdh.util.common.toolbox;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.SneakyThrows;

/**
 * Generic serialization/deserialization class using the Jackson implementation underneath.
 * <p>
 * Supports:
 * <ul>
 *     <li>{@code JSON/JSON5}</li>
 *     <li>{@code XML}</li>
 *     <li>{@code YAML/YML}</li>
 * </ul>
 *
 * @see GsonUtils
 * @see FileUtils
 * @author Kevin Frejdh
 */
@SuppressWarnings({"Duplicates", "unused"})
public class JacksonUtils {

	protected JacksonUtils() {}

	protected static final JsonMapper JSON_MAPPER = defaultConfiguration(JsonMapper.builder())
			.enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
			.enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
			.build();

	protected static final XmlMapper XML_MAPPER = defaultConfiguration(XmlMapper.builder()).build();

	protected static final YAMLMapper YAML_MAPPER = defaultConfiguration(YAMLMapper.builder())
			.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
			.build();

	protected static <M extends ObjectMapper, B extends MapperBuilder<M, B>> B defaultConfiguration(B builder) {
		return builder
				.addModule(new ParameterNamesModule())
				.addModule(new Jdk8Module())
				.addModule(new JavaTimeModule())
				.addModule(new JsonOrgModule())
				.addModule(new JacksonXmlModule())
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * Returns a copy of the default {@code JSON/JSON5} mapper.
	 * @return A new instance.
	 */
	public static JsonMapper getJsonMapper() {
		return JSON_MAPPER.copy();
	}

	/**
	 * Returns a copy of the default {@code XML} mapper.
	 * @return A new instance.
	 */
	public static XmlMapper getXmlMapper() {
		return XML_MAPPER.copy();
	}

	/**
	 * Returns a copy of the default {@code YAML/YML} mapper.
	 * @return A new instance.
	 */
	public static YAMLMapper getYAMLMapper() {
		return YAML_MAPPER.copy();
	}

	/**
	 * Convert an object to a JSON string.
	 * @param obj The object to convert.
	 * @return A JSON string representation.
	 */
	@SneakyThrows
	public static String toJson(Object obj) {
		return JSON_MAPPER.writeValueAsString(obj);
	}

	/**
	 * Convert an object to an XML string.
	 * @param obj The object to convert.
	 * @return An XML string representation.
	 */
	@SneakyThrows
	public static String toXml(Object obj) {
		return XML_MAPPER.writeValueAsString(obj);
	}

	/**
	 * Convert an object to an YAML string.
	 * @param obj The object to convert.
	 * @return An YAML string representation.
	 */
	@SneakyThrows
	public static String toYaml(Object obj) {
		return YAML_MAPPER.writeValueAsString(obj);
	}

	@SneakyThrows
	protected static <E> E deserialize(ObjectMapper objectMapper, String file, TypeReference<E> targetType) {
		String retval = FileUtils.deserializeFromFile(file, String.class);
		try {
			return objectMapper.readValue(retval, targetType);
		} catch (Exception e) {
			throw new IOException("Deserialization failed", e);
		}
	}

	/**
	 * Creates/overrides a filename with serialized data in the JSON format.
	 *
	 * @param file A string setting the filename relative to the resource directory.
	 * @param object The object to save.
	 */
	@SneakyThrows
	public static <E> void serializeToJsonFile(String file, E object) {
		FileUtils.serializeToFile(file, toJson(object));
	}

	/**
	 * Deserializes a filename and returns an object for a JSON file.
	 *
	 * @param file is the string containing the filename.
	 * @param targetType The return type.
	 * @return A new instance of the return type.
	 */
	@SneakyThrows
	public static <E> E deserializeFromJsonFile(String file, TypeReference<E> targetType){
		return deserialize(JSON_MAPPER, file, targetType);
	}

	/**
	 * Deserializes a filename and returns an object for a JSON file.
	 *
	 * @param file is the string containing the filename.
	 * @param targetClass The return class.
	 * @return A new instance of the return type.
	 */
	@SneakyThrows
	public static <E> E deserializeFromJsonFile(String file, Class<E> targetClass) {
		return deserializeFromJsonFile(file, new TypeReference<>() {});
	}

	/**
	 * Creates/overrides a filename with serialized data in the XML format.
	 *
	 * @param file A string setting the filename relative to the resource directory.
	 * @param object The object to save.
	 */
	@SneakyThrows
	public static <E> void serializeToXmlFile(String file, E object) {
		FileUtils.serializeToFile(file, toXml(object));
	}


	/**
	 * Deserializes a filename and returns an object for an XML file.
	 *
	 * @param file is the string containing the filename.
	 * @param targetType The return type.
	 * @return A list of saved series
	 */
	public static <E> E deserializeFromXmlFile(String file, TypeReference<E> targetType){
		return deserialize(XML_MAPPER, file, targetType);
	}

	/**
	 * Deserializes a filename and returns an object for an XML file.
	 *
	 * @param file is the string containing the filename.
	 * @param targetClass The return class.
	 * @return A list of saved series
	 */
	@SneakyThrows
	public static <E> E deserializeFromXmlFile(String file, Class<E> targetClass) {
		return deserializeFromXmlFile(file, new TypeReference<>() {});
	}

	/**
	 * Creates/overrides a filename with serialized data in the XML format.
	 *
	 * @param file A string setting the filename relative to the resource directory.
	 * @param object The object to save.
	 */
	@SneakyThrows
	public static <E> void serializeToYamlFile(String file, E object) {
		FileUtils.serializeToFile(file, toYaml(object));
	}

	/**
	 * Deserializes a filename and returns an object for an XML file.
	 *
	 * @param file is the string containing the filename.
	 * @param targetType The return type.
	 * @return A list of saved series
	 */
	@SneakyThrows
	public static <E> E deserializeFromYamlFile(String file, TypeReference<E> targetType){
		return deserialize(YAML_MAPPER, file, targetType);
	}

	/**
	 * Deserializes a filename and returns an object for an XML file.
	 *
	 * @param file is the string containing the filename.
	 * @param targetClass The return class.
	 * @return A list of saved series
	 */
	@SneakyThrows
	public static <E> E deserializeFromYamlFile(String file, Class<E> targetClass) {
		return deserializeFromYamlFile(file, new TypeReference<>() {});
	}

}
