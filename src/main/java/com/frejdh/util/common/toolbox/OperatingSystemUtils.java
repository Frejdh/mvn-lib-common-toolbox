package com.frejdh.util.common.toolbox;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Util class that detects the OS, and helps abstract the different standards.
 *
 * @author Kevin Frejdh
 */
public class OperatingSystemUtils {
	private volatile static String name;
	private volatile static OperatingSystemType type;

	public enum OperatingSystemType {
		WINDOWS,
		MAC,
		UNIX,
		UNIDENTIFIED
	}

	/**
	 * Get the defined operating system name
	 * @return The name of the OS
	 */
	public static String getName() {
		if (name == null) {
			synchronized (OperatingSystemUtils.class) {
				name = System.getProperty("os.name").toLowerCase();
			}
		}
		return name;
	}

	/**
	 * Return the OS type
	 * @return An enum of the type {@link OperatingSystemType}
	 */
	public static OperatingSystemType getType() {
		if (type == null) {
			synchronized (OperatingSystemUtils.class) {
				if (isWindows())
					type = OperatingSystemType.WINDOWS;
				else if (isMac())
					type = OperatingSystemType.MAC;
				else if (isUnix())
					type = OperatingSystemType.UNIX;
				else
					type = OperatingSystemType.UNIDENTIFIED;
			}
		}
		return type;
	}

	public static boolean isWindows() {
		return getName().contains("win");
	}

	public static boolean isMac() {
		return getName().contains("mac");
	}

	public static boolean isUnix() {
		String name = getName();
		return name.contains("nix") || name.contains("nux") || name.contains("aix") || name.contains("linux");
	}

	/**
	 * Returns the path/directory separator. Backslash for windows like environments, forward slash for the rest.
	 * @return A backslash or forward slash string depending on the OS
	 */
	public static String getPathSeparator() {
		if (isWindows())
			return "\\";
		else
			return "/";
	}

	/**
	 * Concat multiple paths
	 * @param paths Parameter defined as strings
	 * @return A path with a fitting path separator
	 */
	public static String concatPaths(String... paths) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstPath = true;
		for (String p : paths) {
			if (isFirstPath) {
				isFirstPath = false;
			} else {
				sb.append(getPathSeparator());
			}
			sb.append(p);
		}
		return sb.toString();
	}

	/**
	 * Get the directory of a path
	 * @param path Path to inspect
	 * @return The directory or an empty string if not defined
	 */
	public static String getDirectory(String path) {
		if (path.matches(".*[/\\\\].*")) {
			int indexToSplitAt = path.lastIndexOf("/");
			if (indexToSplitAt == -1) {
				indexToSplitAt = path.lastIndexOf("\\");
			}
			return path.substring(0, indexToSplitAt);
		}
		return "";
	}

	/**
	 * Get the directory of a path
	 * @param path Path to inspect
	 * @return The directory
	 */
	public static String getFilename(String path) {
		if (path.matches(".*[/\\\\].*")) {
			int indexToSplitAt = path.lastIndexOf("/");
			if (indexToSplitAt == -1) {
				indexToSplitAt = path.lastIndexOf("\\");
			}
			return path.substring(indexToSplitAt + 1);
		}
		return path;
	}

	/**
	 * Fetch a file from the resource directory.
	 * The given relativePath is autocorrected to work on the current OS. In other words, forward slash can be used on Windows, etc.
	 * @param relativePath The file path (relative from the resource directory)
	 * @return The file or null
	 */
	public static File getFileFromResources(String relativePath) {
		if (getPathSeparator().equals("/")) {
			relativePath = relativePath.replace("\\", getPathSeparator());
		}
		else {
			relativePath = relativePath.replace("/", getPathSeparator());
		}

		try {
			return Paths.get(OperatingSystemUtils.class.getClassLoader().getResource(relativePath).toURI()).toFile();
		} catch (NullPointerException | URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Compliant with Windows, UNIX and filesystem naming schemes.
	 * Replaces characters that are unsupported to a fully functional unicode variant.
	 *
	 * @param filename Filename to replace characters of. <u>Note, this cannot be a full path!</u>
	 * @return A new string with the illegal characters replaced
	 */
	public static String replaceIllegalFilenameCharacters(String filename) {
		if (filename == null) {
			return null;
		}

		filename = filename.replace(":", "꞉"); // Modifier Letter Colon, U+A789
		filename = filename.replace("/", " ∕ "); // With spacing (hard to read otherwise)
		filename = filename.replace("\\", "＼");
		filename = filename.replace("*", "⁎");
		filename = filename.replace("<", "‹").replace(">", "›");
		filename = filename.replace("|", "⏐");
		filename = filename.replace("?", "？"); // Adds some spacing, not pretty but it works
		filename = filename.replace("\"", "”");
		return filename;
	}

}
