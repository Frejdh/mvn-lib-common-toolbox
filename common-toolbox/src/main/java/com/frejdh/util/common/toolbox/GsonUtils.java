package com.frejdh.util.common.toolbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

/**
 * Generic serialization/deserialization class using the GSON implementation underneath.
 * <p>
 * Supports:
 * <ul>
 *     <li>{@code JSON}</li>
 * </ul>
 *
 * @see JacksonUtils
 * @see FileUtils
 * @author Kevin Frejdh
 */
@SuppressWarnings({"Duplicates", "unused", "ResultOfMethodCallIgnored"})
public class GsonUtils {
	protected GsonUtils() {}

	private static final Gson GSON = new GsonBuilder()
			.setLenient()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapter(Calendar.class, new DateUtils.GregorianCalendarDeserializer())
			.create();

	/**
	 * @deprecated Please use {@link FileUtils#serializeToFile(String, String, Object)} instead.
	 */
	@Deprecated(forRemoval = true, since = "2.0.0")
	public static <E> void serializeToFile(String directory, String filename, E object) throws IOException {
		FileUtils.serializeToFile(directory, filename, object);
	}

	/**
	 * @deprecated Please use {@link FileUtils#serializeToFile(String, Object)} instead.
	 */
	@Deprecated(forRemoval = true, since = "2.0.0")
	public static <E> void serializeToFile(String relativeOrAbsoluteFile, E object) throws IOException {
		FileUtils.serializeToFile(relativeOrAbsoluteFile, object);
	}

	/**
	 * @deprecated Please use {@link FileUtils#deserializeFromFile(String, Class)} instead.
	 */
	@Deprecated(forRemoval = true, since = "2.0.0")
	public static <E> E deserializeFromFile(String relativeFilename, Class<E> returnType) throws IOException, ClassNotFoundException {
		return deserializeFromFile(null, relativeFilename, returnType);
	}

	/**
	 * Deserializes a filename and returns an object. If text, a String is returned.
	 *
	 * @param filename is the string containing the filename.
	 * @return A list of saved series
	 * @throws IOException            Throws IOException if the IO is interrupted
	 * @throws ClassNotFoundException If the data in the filename does not represent a java object.
	 * @deprecated Please use {@link FileUtils#deserializeFromFile(String, String, Class)} instead.
	 */
	@Deprecated(forRemoval = true, since = "2.0.0")
	@SuppressWarnings("unchecked")
	public static <E> E deserializeFromFile(String directory, String filename, Class<E> returnType) throws IOException, ClassNotFoundException {
		String path = (directory != null ? directory + OperatingSystemUtils.getPathSeparator() : "") + filename;

		try (FileInputStream inputFile = new FileInputStream(path); ObjectInputStream objIn = new ObjectInputStream(inputFile)) {
			return (E) objIn.readObject();
		}
	}

	/**
	 * Creates/overrides a filename with serialized data in JSON format.
	 *
	 * @param filename A string setting the filename.
	 * @param object   An object containing the object to save.
	 * @throws IOException Throws an IOException if the IO is interrupted
	 */
	public static <E> void serializeToJsonFile(String directory, String filename, E object) throws IOException {
		serializeToFile(directory, filename, GSON.toJson(object));
	}

	/**
	 * Creates/overrides a filename with serialized data in JSON format.
	 *
	 * @param relativeFilename A string setting the filename relative to the resource directory.
	 * @param object   An object containing the object to save.
	 * @throws IOException Throws an IOException if the IO is interrupted
	 */
	public static <E> void serializeToJsonFile(String relativeFilename, E object) throws IOException {
		serializeToFile(relativeFilename, GSON.toJson(object));
	}

	/**
	 * Deserializes a filename and returns an object for a JSON file.
	 *
	 * @param relativeFilename A string setting the filename relative to the resource directory.
	 * @return A list of saved series
	 * @throws IOException            Throws IOException if the IO is interrupted
	 * @throws ClassNotFoundException If the data in the filename does not represent a java object.
	 */
	public static <E> E deserializeFromJsonFile(String relativeFilename, Class<E> targetClass) throws IOException, ClassNotFoundException {
		return deserializeFromJsonFile(null, relativeFilename, targetClass);
	}

	/**
	 * Deserializes a filename and returns an object for a JSON file.
	 *
	 * @param filename is the string containing the filename.
	 * @return A list of saved series
	 * @throws IOException            Throws IOException if the IO is interrupted
	 * @throws ClassNotFoundException If the data in the filename does not represent a java object.
	 */
	public static <E> E deserializeFromJsonFile(String directory, String filename, Class<E> targetClass) throws IOException, ClassNotFoundException {
		String retval = deserializeFromFile(directory, filename, String.class);
		try {
			return GSON.fromJson(retval, targetClass);
		} catch (Exception e) {
			throw new IOException("Couldn't load JSON", e);
		}
	}
}
