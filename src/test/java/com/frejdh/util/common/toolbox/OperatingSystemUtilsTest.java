package com.frejdh.util.common.toolbox;

import com.frejdh.util.common.toolbox.OperatingSystemUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.*;

/**
 * Function tests
 */
public class OperatingSystemUtilsTest {

	private static final String PARAMETERIZED_TEST_NAME = DISPLAY_NAME_PLACEHOLDER + "[" + INDEX_PLACEHOLDER + "] -> [" + ARGUMENTS_WITH_NAMES_PLACEHOLDER + "]";

	@ParameterizedTest(name = PARAMETERIZED_TEST_NAME)
	@CsvSource({
			"C:\\My super awesome path, C:\\My super awesome path\\filename.txt",
			"'', filename.txt",
			"/home/user, /home/user/file",
			"/home/user/directory1, /home/user/directory1/"
	})
	void directoryPathTest(String expectedDirectory, String suppliedDirectoryOrFilename) {
		assertEquals(expectedDirectory, OperatingSystemUtils.getDirectory(suppliedDirectoryOrFilename),
				String.format("Expected '%s' to be '%s'", expectedDirectory, suppliedDirectoryOrFilename)
		);
	}

	@ParameterizedTest(name = PARAMETERIZED_TEST_NAME)
	@CsvSource({
			"filename.txt, C:\\My super awesome path\\filename.txt",
			"filename.txt, filename.txt",
			"file, /home/user/file",
			"'', /home/user/directory1/"
	})
	void filenamePathTest(String expectedFilename, String suppliedDirectoryOrFilename) {
		assertEquals(expectedFilename, OperatingSystemUtils.getFilename(suppliedDirectoryOrFilename),
				String.format("Expected '%s' to be '%s'", expectedFilename, suppliedDirectoryOrFilename)
		);
	}

}
