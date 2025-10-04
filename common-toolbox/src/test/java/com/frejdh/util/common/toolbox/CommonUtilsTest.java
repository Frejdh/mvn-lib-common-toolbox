package com.frejdh.util.common.toolbox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommonUtilsTest {

	@CsvSource(delimiter = '|', value = {
			"15|true|true",
			"-15|true|true",
			"-15.0|true|true",
			"15.0|true|true",
			"15.0|false|true",
			"15,0|true|true",
			"15,0|false|false",
			"01|true|true",
			"|true|false",
			"-nonNumeric|true|false",
			"nonNumeric|true|false"
	})
	@ParameterizedTest
	void isNumeric(String input, boolean allowDecimalCharacter, boolean expectedValue) {
		assertEquals(expectedValue, CommonUtils.isNumeric(input, allowDecimalCharacter));
	}

	@CsvSource(delimiter = '|', value = {
			"105|105",
			"-105|-105",
			"-105.004|-105",
			"-105,004|-105",
			"105.004|105",
			"008|8",
			"|",
			"non-numeric|",
	})
	@ParameterizedTest
	void toBigInteger(String input, String expectedValueAsString) {
		BigInteger value = CommonUtils.toBigInteger(input);
		assertEquals(expectedValueAsString, Objects.toString(value, null));
	}

	@CsvSource(delimiter = '|', value = {
			"105|105.0",
			"-105|-105.0",
			"-105.004|-105.004",
			"-105,004|-105.004",
			"105.004|105.004",
			"008|8.0",
			"|",
			"non-numeric|",
	})
	@ParameterizedTest
	void toBigDecimal(String input, String expectedValueAsString) {
		BigDecimal value = CommonUtils.toBigDecimal(input);
		assertEquals(expectedValueAsString, (value != null) ? value.toPlainString() : null);
	}

	@CsvSource(delimiter = '|', value = {
			"105|105.0",
			"-105|-105.0",
			"-105.004|-105.004",
			"105.004|105.004",
			"008|8.0",
			"|",
			"non-numeric|",
	})
	@ParameterizedTest
	void toDouble(String input, String expectedValueAsString) {
		Double value = CommonUtils.toDouble(input);
		assertEquals(expectedValueAsString, Objects.toString(value, null));
	}

	@CsvSource(delimiter = '|', value = {
			"105|105",
			"-105|-105",
			"-105.004|-105",
			"105.004|105",
			"008|8",
			"|",
			"non-numeric|",
			"2147483648|"
	})
	@ParameterizedTest
	void toInteger(String input, String expectedValueAsString) {
		Integer value = CommonUtils.toInteger(input);
		assertEquals(expectedValueAsString, Objects.toString(value, null));
	}

	@CsvSource(delimiter = '|', value = {
			"105|105",
			"-105|-105",
			"-105.004|-105",
			"105.004|105",
			"008|8",
			"|",
			"non-numeric|",
			"2147483648|2147483648"
	})
	@ParameterizedTest
	void toLong(String input, String expectedValueAsString) {
		Long value = CommonUtils.toLong(input);
		assertEquals(expectedValueAsString, Objects.toString(value, null));
	}
//
//	@Test
//	void softMerge() {
//	}
//
//	@Test
//	void softMergeInverse() {
//	}
//
//	@Test
//	void overrideMerge() {
//	}
//
//	@Test
//	void removeListDuplicatesByFieldName() {
//	}
//
//	@Test
//	void softMergeDuplicates() {
//	}
//
//	@Test
//	void allFieldsEquals() {
//	}
//
//	@Test
//	void allFieldsAreEqual() {
//	}
//
//	@Test
//	void recastObject() {
//	}
//
//	@Test
//	void getCallingMethodName() {
//	}
//
//	@Test
//	void getMethodName() {
//	}
//
//	@Test
//	void stacktraceToString() {
//	}
//
//	@Test
//	void testStacktraceToString() {
//	}
//
//	@Test
//	void prependToArray() {
//	}
//
//	@Test
//	void appendToArray() {
//	}

	@Test
	void arrayToString() {
		assertEquals("[a, b, c]", CommonUtils.arrayToString(new String[] { "a", "b", "c" }));
		assertEquals("[a, b, c, null]", CommonUtils.arrayToString(new String[] { "a", "b", "c", null }));
		assertEquals("[]", CommonUtils.arrayToString(new String[] { }));
		assertEquals("[]", CommonUtils.arrayToString(null));
	}

	@Test
	void stringToList() {
		// Use default separator
		assertEquals(List.of("a", "b", "c", ""), CommonUtils.stringToList("a,b,c,"));
		assertEquals(List.of("a", "b", "c"), CommonUtils.stringToList("a,b,c"));

		// Use custom separator
		assertEquals(List.of("a", "b,", "c", ""), CommonUtils.stringToList("a|b,|c|", "|"));
		assertEquals(List.of("a", "b,", "c"), CommonUtils.stringToList("a|b,|c", "|"));

		// Null-safety
		assertEquals(List.of(), CommonUtils.stringToList(null));
	}

	@Test
	void sneakyThrow() {
		Exception nonRuntimeException = new IOException();
		assertThrows(IOException.class, () -> CommonUtils.sneakyThrow(nonRuntimeException));
	}

	@Test
	void wrapAsRuntimeException() {
		String errorMsg = "Some reason";
		IOException nonRuntimeException = new IOException(errorMsg);
		RuntimeException wrappedException = CommonUtils.wrapAsRuntimeException(nonRuntimeException);

		assertEquals(nonRuntimeException, wrappedException.getCause());
		assertEquals(errorMsg, wrappedException.getMessage());
	}

}