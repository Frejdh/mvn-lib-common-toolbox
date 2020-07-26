package com.frejdh.util.common.toolbox;

/**
 * Util class that detects the OS and helps abstracting their different standards.
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

}
