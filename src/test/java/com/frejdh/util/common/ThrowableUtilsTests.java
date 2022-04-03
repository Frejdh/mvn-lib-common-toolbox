package com.frejdh.util.common;

import com.frejdh.util.common.invocations.ThrowableUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThrowableUtilsTests {

	@Test
	public void equalsToAnyWorks() {
		final String expectedValue = "yay!";

		String retval = ThrowableUtils.when(() -> "old")
				.equalsToAny("random1", "old", "random2")
				.thenReturn(expectedValue)
				.execute();
		assertEquals(expectedValue, retval);
	}

}
