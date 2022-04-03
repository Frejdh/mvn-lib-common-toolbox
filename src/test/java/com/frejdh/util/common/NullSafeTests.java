package com.frejdh.util.common;

import com.frejdh.util.common.invocations.NullSafe;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NullSafeTests {

	@Test
	public void firstNonNullCanHandleNullPointerExceptionsCorrectly() {
		final String expectedValue = "yay!";
		final AtomicLong invocationCounter = new AtomicLong(0);

		String retval = NullSafe.firstNonNull(
				() -> {
					incrementInvocationCounter(invocationCounter, null);
					throw new NullPointerException("oops!");
				},
				() -> {
					incrementInvocationCounter(invocationCounter, null);
					throw new NullPointerException("oh no, not again!");
				},
				() -> incrementInvocationCounter(invocationCounter, expectedValue),
				() -> {
					incrementInvocationCounter(invocationCounter, null);
					throw new NullPointerException("oh no, not again 2!");
				},
				() -> {
					incrementInvocationCounter(invocationCounter, null);
					throw new NullPointerException("oh no, not again 3!");
				}
		);

		long expectedNumberOfInvocations = 3; // 3rd lambda has value
		assertEquals(expectedValue, retval);
		assertEquals(expectedNumberOfInvocations, invocationCounter.get());
	}

	@Test
	public void firstNonNullCanHandleNullValuesCorrectly() {
		final String expectedValue = "yay!";
		final AtomicLong invocationCounter = new AtomicLong(0);

		String retval = NullSafe.firstNonNull(
				() -> incrementInvocationCounter(invocationCounter, null),
				() -> incrementInvocationCounter(invocationCounter, expectedValue),
				() -> incrementInvocationCounter(invocationCounter, null),
				() -> incrementInvocationCounter(invocationCounter, null)
		);

		long expectedNumberOfInvocations = 2; // 2nd lambda has value
		assertEquals(expectedValue, retval);
		assertEquals(expectedNumberOfInvocations, invocationCounter.get());
	}

	private <T> T incrementInvocationCounter(AtomicLong invocationCounter, T retval) {
		invocationCounter.incrementAndGet();
		return retval;
	}

}
