package com.frejdh.util.common.toolbox;

import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Handles different environment variables set by different frameworks. Currently handles:
 * - Java (stock)
 * - Spring / Spring-boot
 * - Vertx
 */
@SuppressWarnings("SameParameterValue")
public class EnvironmentUtils {

	private static volatile boolean isRuntimeEnabled = false;
	private static volatile boolean isInitialized = false;
	private static volatile Properties environmentVariables = new Properties(System.getProperties());
	private static volatile Set<String> filesToLoad = new HashSet<>();

	static {
		setDefaultFilesToLoad();
		loadEnvironmentVariables();
	}

	/**
	 * Set the default files to load
	 */
	private static void setDefaultFilesToLoad() {
		// Spring
		filesToLoad.add(System.getProperty("java.class.path") + "/application.properties");

		// Vertx
		filesToLoad.add(System.getProperty("java.class.path") + "/conf/config.json");
	}

	private static void loadEnvironmentVariables() {
		if (shouldLoadEnvVariables()) {
			synchronized (EnvironmentUtils.class) {
				if (!shouldLoadEnvVariables())
					return;

				loadVariablesFromFiles();
				isInitialized = true;
			}
		}
	}

	private static synchronized boolean shouldLoadEnvVariables() {
		return !isInitialized || isRuntimeEnabled;
	}

	protected static synchronized boolean isInitialized() {
		return isInitialized;
	}

	protected static synchronized boolean isRuntimeEnabled() {
		return isRuntimeEnabled;
	}

	protected static synchronized void setInitialized(boolean isInitialized) {
		EnvironmentUtils.isInitialized = isInitialized;
	}

	protected static synchronized void setRuntimeEnabled(boolean isRuntimeEnabled) {
		EnvironmentUtils.isRuntimeEnabled = isRuntimeEnabled;
	}

	private static void loadVariablesFromFiles() {
		for (String file : filesToLoad) {
			if (file.matches("(?i).*[.]json$")) { // Ends with .json (case-insensitive)
				//SerializeUtils.deserializeFromFile()
			}
			else { // Not json, read values by the format 'variable=true' format, line per line

			}
		}
	}


}
