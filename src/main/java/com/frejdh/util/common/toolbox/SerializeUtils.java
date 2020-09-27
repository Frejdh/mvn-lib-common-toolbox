package com.frejdh.util.common.toolbox;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Calendar;

/**
 * Generic serialize-handler class.
 *
 * @author Kevin Frejdh
 */
@SuppressWarnings({"Duplicates", "unused"})
public class SerializeUtils {

	private static final Gson GSON = new GsonBuilder()
			.setLenient()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapter(Calendar.class, new DateUtils.GregorianCalendarDeserializer())
			.create();

	/**
	 * Creates/overrides a filename with serialized data.
	 *
	 * @param filename A string setting the filename.
	 * @param object   An object containing the object to save.
	 * @throws IOException Throws IOException if the IO is interrupted
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static <E> void serializeToFile(String directory, String filename, E object) throws IOException {
		if (directory != null) {
			new File(directory).mkdirs();
		}
		String path = (directory != null ? directory + OperatingSystemUtils.getPathSeparator() : "") + filename;

		FileOutputStream outputFile = null;
		ObjectOutputStream objOut = null;
		try {
			outputFile = new FileOutputStream(path);
			objOut = new ObjectOutputStream(outputFile);
			objOut.writeObject(object); // Handles String and serializable
		} finally {
			try {
				if (objOut != null) {
					objOut.close();
				}
				if (outputFile != null) {
					outputFile.close();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	/**
	 * Creates/overrides a filename with serialized data.
	 *
	 * @param relativeFilename Filename relative from the resource folder.
	 * @param object   An object containing the object to save.
	 * @throws IOException Throws IOException if the IO is interrupted
	 */
	public static <E> void serializeToFile(String relativeFilename, E object) throws IOException {
		serializeToFile(null, relativeFilename, object);
	}

	/**
	 * Deserializes a filename and returns an object. If text, a String is returned.
	 *
	 * @param relativeFilename Filename relative from the resource folder.
	 * @return A list of saved series
	 * @throws IOException            Throws IOException if the IO is interrupted
	 * @throws ClassNotFoundException If the data in the filename does not represent a java object.
	 */
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
	 */
	@SuppressWarnings("unchecked")
	public static <E> E deserializeFromFile(String directory, String filename, Class<E> returnType) throws IOException, ClassNotFoundException {
		String path = (directory != null ? directory + OperatingSystemUtils.getPathSeparator() : "") + filename;

		FileInputStream inputFile = null;
		ObjectInputStream objIn = null;

		try {
			inputFile = new FileInputStream(path);
			objIn = new ObjectInputStream(inputFile);
			return (E) objIn.readObject();
		} finally {
			try {
				if (objIn != null) {
					objIn.close();
				}
				if (inputFile != null) {
					inputFile.close();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
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
