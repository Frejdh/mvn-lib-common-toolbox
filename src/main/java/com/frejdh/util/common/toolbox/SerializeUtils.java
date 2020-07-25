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

	/**
	 * Creates/overrides a filename with serialized data.
	 *
	 * @param filename A string setting the filename.
	 * @param object   An object containing the object to save.
	 * @throws IOException Throws IOException if the IO is interrupted
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static <E> void serializeToFile(String directory, String filename, E object) throws IOException {
		new File(directory).mkdirs();
		String path = directory + OperatingSystemUtils.getPathSeparator() + filename;
		File pathFile = new File(path);

		FileOutputStream outputFile = null;
		ObjectOutputStream objOut = null;
		try {
			outputFile = new FileOutputStream(pathFile);
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
	 * Deserializes a filename and returns an object. If text, a String is returned.
	 *
	 * @param filename is the string containing the filename.
	 * @return A list of saved series
	 * @throws IOException            Throws IOException if the IO is interrupted
	 * @throws ClassNotFoundException If the data in the filename does not represent a java object.
	 */
	@SuppressWarnings("unchecked")
	public static <E> E deserializeFromFile(String directory, String filename) throws IOException, ClassNotFoundException {
		String path = directory + OperatingSystemUtils.getPathSeparator() + filename;
		File pathFile = new File(path);

		FileInputStream inputFile = null;
		ObjectInputStream objIn = null;

		try {
			inputFile = new FileInputStream(pathFile);
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
		serializeToFile(directory, filename,
				new GsonBuilder()
						.setPrettyPrinting()
						.disableHtmlEscaping()
						.registerTypeAdapter(Calendar.class, new DateUtils.GregorianCalendarDeserializer())
						.create()
						.toJson(object));
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
		Object retval = deserializeFromFile(directory, filename);
		if (retval instanceof String) {
			try {
				return new Gson().fromJson((String) retval, targetClass);
			} catch (Exception e) {
				throw new IOException("Couldn't load JSON", e);
			}
		}
		else {
			throw new IOException("The file wasn't a JSON string");
		}
	}
}