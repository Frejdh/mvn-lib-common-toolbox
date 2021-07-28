package com.frejdh.util.common;

import com.frejdh.util.common.exceptions.AlreadySetException;
import com.frejdh.util.common.toolbox.OperatingSystemUtils;
import com.frejdh.util.common.toolbox.ReflectionUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Function tests
 */
public class OperatingSystemUtilsTests {

	@Test
	public void directoryPathTest() {
		int index = 1;
		assertEquals("Test " + index++, "C:\\My super awesome path", OperatingSystemUtils.getDirectory("C:\\My super awesome path\\filename.txt"));
		assertEquals("Test " + index++, "", OperatingSystemUtils.getDirectory("filename.txt"));
		assertEquals("Test " + index++, "/home/user", OperatingSystemUtils.getDirectory("/home/user/file"));
		assertEquals("Test " + index, "/home/user/directory1", OperatingSystemUtils.getDirectory("/home/user/directory1/"));
	}

	@Test
	public void filenamePathTest() {
		int index = 1;
		assertEquals("Test " + index++, "filename.txt", OperatingSystemUtils.getFilename("C:\\My super awesome path\\filename.txt"));
		assertEquals("Test " + index++, "filename.txt", OperatingSystemUtils.getFilename("filename.txt"));
		assertEquals("Test " + index++, "file", OperatingSystemUtils.getFilename("/home/user/file"));
		assertEquals("Test " + index, "", OperatingSystemUtils.getFilename("/home/user/directory1/"));
	}
}
