package com.frejdh.util.common.invocations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConditionalsTest {

	private static final String EXPECTED_VALUE = "expectedValue";

	@Test
	void throwsAnyExceptionIsOk() {
		Object retval = Conditionals.when(() -> {
					throw new NullPointerException("");
				})
				.throwsAnyException()
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertEquals(EXPECTED_VALUE, retval);

		Conditionals.when("").execute();
	}

	@Test
	void throwingSpecificExceptionsCanBeHandledAndCanBeStacked() {
		Object retval = Conditionals.when(() -> {
					throw new NullPointerException();
				})
				.throwsException(IllegalStateException.class)
				.thenReturn("Some unexpected error 1")
				.throwsException(NullPointerException.class)
				.thenReturn(EXPECTED_VALUE)
				.throwsException(NullPointerException.class)
				.thenReturn("Shall never be reached as it should be handled above already")
				.execute();
		assertEquals(EXPECTED_VALUE, retval);
	}

	@Test
	void unhandledExceptionsAreStillThrown() {
		assertThrows(RuntimeException.class, () -> Conditionals.when(() -> {
					throw new RuntimeException();
				})
				.throwsException(NullPointerException.class)
				.thenReturn(EXPECTED_VALUE)
				.execute()
		);
	}

	@Test
	void throwsAnyExceptionProducesNoErrorWhenNothingIsCaught() {
		String retval = Conditionals.when(() -> EXPECTED_VALUE)
				.throwsAnyException()
				.thenReturn("ERROR")
				.execute();
		assertEquals(EXPECTED_VALUE, retval);
	}

	@Test
	void reThrowsToAnotherExceptionWhenSpecified() {
		assertThrows(IOException.class, () -> Conditionals.when(() -> {
					throw new IllegalStateException();
				})
				.throwsAnyException()
				.thenThrow(new IOException())
				.execute()
		);
	}

	@NullSource
	@EmptySource
	@ParameterizedTest
	void equalsToBlankWhenProvidingBlankValue(String value) {
		String retvalForMatchingPredicateValue = Conditionals.when(() -> value)
				.equalsToBlank()
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertEquals(EXPECTED_VALUE, retvalForMatchingPredicateValue);
	}

	private static Stream<Arguments> nonBlankValues() {
		return Stream.of(
				Arguments.of("Non-empty string"),
				Arguments.of(new Object()),
				Arguments.of(true)
		);
	}

	@MethodSource("nonBlankValues")
	@ParameterizedTest
	void equalsToBlankWhenProvidingNonBlankValue(Object value) {
		Object retvalForMatchingPredicateValue = Conditionals.when(value)
				.equalsToBlank()
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertNull(retvalForMatchingPredicateValue);
	}

	@Test
	void equalsToAny() {
		String valueToMatch = "matching";

		String retvalForMatchingEqualsValue = Conditionals.when(() -> valueToMatch)
				.equalsToAny("random1", valueToMatch, "random2")
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertEquals(EXPECTED_VALUE, retvalForMatchingEqualsValue);

		String retvalForNoMatchingEqualsValue = Conditionals.when(() -> valueToMatch)
				.equalsToAny("random1", "random2")
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertNull(retvalForNoMatchingEqualsValue);
	}

	@Test
	void fulfills() {
		String predicateValue = "MATCHING VALUE";
		Predicate<String> predicate = value -> value.equals(predicateValue);

		String retvalForMatchingPredicateValue = Conditionals.when(predicateValue)
				.fulfills(predicate)
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertEquals(EXPECTED_VALUE, retvalForMatchingPredicateValue);

		String retvalForNoMatchingPredicateValue = Conditionals.when(predicateValue)
				.fulfills(predicate.negate())
				.thenReturn(EXPECTED_VALUE)
				.execute();
		assertNull(retvalForNoMatchingPredicateValue);
	}

}
