package com.frejdh.util.common.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

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

	// One of the faster ways to convert a stream.
	public static String inputStreamToString(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}

		try {
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[4 * 0x400]; // 4KB
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}

			return result.toString(StandardCharsets.UTF_8.name());
		} catch (NullPointerException | IOException e ) {
			return null;
		}
	}

}
