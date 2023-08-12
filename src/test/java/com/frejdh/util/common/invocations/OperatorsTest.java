package com.frejdh.util.common.invocations;

import com.frejdh.util.common.functional.ThrowingSupplier;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperatorsTest {

	@Test
	void firstNonNullCanHandleNullPointerExceptionsCorrectly() {
		final String expectedValue = "yay!";
		final AtomicLong invocationCounter = new AtomicLong(0);

		String retval = Operators.firstNonNull(
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
	void firstNonNullCanHandleNullValuesCorrectly() {
		final String expectedValue = "yay!";
		final AtomicLong invocationCounter = new AtomicLong(0);

		String retval = Operators.firstNonNull(
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

	@Test
	void safeCallsReturnsValueWhenPossible() {
		Pojo pojo = new Pojo();
		String existingValue = pojo.a.b.c;
		assertNotNull(existingValue);
		assertEquals(existingValue, Operators.safeCall(() -> pojo.a.b.c));
	}

	@Test
	void safeCallsReturnsNullWhenValueDoesNotExist() {
		Pojo pojo = new Pojo();
		ThrowingSupplier<String> supplier = () -> pojo.a.b.c;
		assertNotNull(Operators.safeCall(supplier));

		pojo.a.b.c = null;
		assertNull(Operators.safeCall(supplier));

		pojo.a.b = null;
		assertNull(Operators.safeCall(supplier));

		pojo.a = null;
		assertNull(Operators.safeCall(supplier));

		assertNull(Operators.safeCall(() -> null));
	}

	@Test
	void elvisReturnsDefaultValueWhenNullIsEncountered() {
		String expectedRetval = "RETVAL";
		Pojo pojo = new Pojo();
		pojo.a.b.c = null;

		assertEquals(expectedRetval, Operators.elvis(() -> pojo.a.b.c, expectedRetval));
		assertEquals(expectedRetval, Operators.elvis(() -> null, expectedRetval));
	}

	public static class Pojo {
		A a = new A();

		public static class A {
			B b = new B();
		}

		public static class B {
			String c = "test";
		}

	}

}
