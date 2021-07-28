package com.frejdh.util.common;

import com.frejdh.util.common.toolbox.ReflectionUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReflectionUtilsTests {

	@Test
	public void canGetInstanceVariable() throws Exception {
		ReflectionTestClass exception = new ReflectionTestClass();
		String msg = ReflectionUtils.getVariable(exception, "finalVariable", String.class);
		assertEquals("final", msg);
	}

	@Test
	public void canGetStaticVariable() throws Exception {
		ReflectionTestClass exception = new ReflectionTestClass();
		String msg = ReflectionUtils.getVariable(ReflectionTestClass.class, "staticFinalVariable", String.class);
		assertEquals("staticFinal", msg);
	}

	public static class ReflectionTestClass {
		private final String finalVariable = "final";
		private static final String staticFinalVariable = "staticFinal";
	}

}
