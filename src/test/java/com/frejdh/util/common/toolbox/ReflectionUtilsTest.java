package com.frejdh.util.common.toolbox;

import com.frejdh.util.common.toolbox.ReflectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionUtilsTest {

	@Test
	@EnabledForJreRange(min = JRE.JAVA_8, max = JRE.JAVA_11)
	void canGetInstanceVariable() throws Exception {
		ReflectionTestClass exception = new ReflectionTestClass();
		String msg = ReflectionUtils.getVariable(exception, "finalVariable", String.class);
		assertEquals("final", msg);
	}

	@Test
	@EnabledForJreRange(min = JRE.JAVA_8, max = JRE.JAVA_11)
	void canGetStaticVariable() throws Exception {
		ReflectionTestClass exception = new ReflectionTestClass();
		String msg = ReflectionUtils.getVariable(ReflectionTestClass.class, "staticFinalVariable", String.class);
		assertEquals("staticFinal", msg);
	}

	public static class ReflectionTestClass {
		private final String finalVariable = "final";
		private static final String staticFinalVariable = "staticFinal";
	}

}
