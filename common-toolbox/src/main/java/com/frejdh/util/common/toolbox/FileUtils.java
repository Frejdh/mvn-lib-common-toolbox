package com.frejdh.util.common.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Generic utils class for file handling.
 *
 * @see JacksonUtils
 * @see GsonUtils
 * @author Kevin Frejdh.
 */
public class FileUtils {

	protected FileUtils() {}

	/**
	 * Load a file as an InputStream
	 * @param absolutePath The absolute path for the file
	 * @return An InputStream or null
	 */
	public static InputStream getFileAsStream(String absolutePath) {
		try {
			return new FileInputStream(absolutePath);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Load a file as an string
	 * @param absolutePath The absolute path for the file
	 * @return A string or null
	 */
	public static String getFileAsString(String absolutePath) {
		return inputStreamToString(getFileAsStream(absolutePath));
	}

	/**
	 * Load a file as an InputStream
	 * @param relativePath The relative path from the resource directory
	 * @return An InputStream or null
	 */
	public static InputStream getResourceFileAsStream(String relativePath) {
		return FileUtils.class.getResourceAsStream(!relativePath.startsWith("/") ? "/" + relativePath : relativePath);
	}

	/**
	 * Load a file as a string.
	 * @param relativePath The relative path from the resource directory
	 * @return A string or null if the file couldn't be loaded
	 */
	public static String getResourceFileAsString(String relativePath) {
		return inputStreamToString(getResourceFileAsStream(relativePath));
	}

	/**
	 * Convert a given {@link InputStream} instance to a regular {@link String}.
	 * @return A string representation of the stream.
	 */
	public static String inputStreamToString(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}

		// One of the faster ways to convert a stream.
		try {
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[4 * 0x400]; // 4KB
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}

			return result.toString(StandardCharsets.UTF_8);
		} catch (NullPointerException | IOException e ) {
			return null;
		}
	}

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

		try (FileOutputStream outputFile = new FileOutputStream(path); ObjectOutputStream objOut = new ObjectOutputStream(outputFile)) {
			objOut.writeObject(object); // Handles String and serializable
		}
	}

	/**
	 * Creates/overrides a filename with serialized data.
	 *
	 * @param relativeOrAbsoluteFilePath Filename relative from the resource folder.
	 * @param object   An object containing the object to save.
	 * @throws IOException Throws IOException if the IO is interrupted
	 */
	public static <E> void serializeToFile(String relativeOrAbsoluteFilePath, E object) throws IOException {
		serializeToFile(null, relativeOrAbsoluteFilePath, object);
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

		try (FileInputStream inputFile = new FileInputStream(path); ObjectInputStream objIn = new ObjectInputStream(inputFile)) {
			return (E) objIn.readObject();
		}
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

}
