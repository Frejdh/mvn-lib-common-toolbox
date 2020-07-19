package com.frejdh.util.common.toolbox;

/**
 * Stores locations for saved files by Operating system standards.
 */
public class OperatingSystemUtils {
	private volatile static String name;
	private volatile static OperatingSystemEnum type;

	public enum OperatingSystemEnum {WINDOWS, MAC, UNIX, UNIDENTIFIED}

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
	 * @return An enum of the type {@link OperatingSystemEnum}
	 */
	public static OperatingSystemEnum getType() {
		if (type == null) {
			synchronized (OperatingSystemUtils.class) {
				if (isWindows())
					type = OperatingSystemEnum.WINDOWS;
				else if (isMac())
					type = OperatingSystemEnum.MAC;
				else if (isUnix())
					type = OperatingSystemEnum.UNIX;
				else
					type = OperatingSystemEnum.UNIDENTIFIED;
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

}
