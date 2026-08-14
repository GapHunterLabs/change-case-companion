package dev.gaphunter.changecasecompanion.convert

import dev.gaphunter.changecasecompanion.model.CaseStyle
import junit.framework.TestCase

class CaseConverterTest : TestCase() {

    fun testWordsSplitsCamelCase() {
        assertEquals(listOf("my", "Variable", "Name"), CaseConverter.words("myVariableName"))
    }

    fun testWordsSplitsPascalCase() {
        assertEquals(listOf("My", "Class", "Name"), CaseConverter.words("MyClassName"))
    }

    fun testWordsSplitsSnakeCase() {
        assertEquals(listOf("my", "variable", "name"), CaseConverter.words("my_variable_name"))
    }

    fun testWordsSplitsKebabCase() {
        assertEquals(listOf("my", "variable", "name"), CaseConverter.words("my-variable-name"))
    }

    fun testWordsSplitsConstantCase() {
        assertEquals(listOf("MY", "CONSTANT", "NAME"), CaseConverter.words("MY_CONSTANT_NAME"))
    }

    fun testWordsHandlesAcronymsCorrectly() {
        // The acronym run belongs together, not split letter-by-letter.
        assertEquals(listOf("XML", "Http", "Request"), CaseConverter.words("XMLHttpRequest"))
        assertEquals(listOf("parse", "JSON", "Body"), CaseConverter.words("parseJSONBody"))
    }

    fun testWordsKeepsDigitsAttachedToTheirAdjacentLowercaseRun() {
        // "http2" stays one word, matching real-world expectations (and
        // the well-known JS "change-case" libraries' own behavior) --
        // splitting into "http"/"2"/"server" would be more aggressive
        // than any actual user wants for an identifier like this.
        assertEquals(listOf("http2", "Server"), CaseConverter.words("http2Server"))
    }

    fun testConvertToCamelCase() {
        assertEquals("myVariableName", CaseConverter.convert("my_variable_name", CaseStyle.CAMEL))
        assertEquals("myVariableName", CaseConverter.convert("MY_VARIABLE_NAME", CaseStyle.CAMEL))
        assertEquals("xmlHttpRequest", CaseConverter.convert("XMLHttpRequest", CaseStyle.CAMEL))
    }

    fun testConvertToPascalCase() {
        assertEquals("MyVariableName", CaseConverter.convert("my_variable_name", CaseStyle.PASCAL))
        assertEquals("XmlHttpRequest", CaseConverter.convert("XMLHttpRequest", CaseStyle.PASCAL))
    }

    fun testConvertToSnakeCase() {
        assertEquals("my_variable_name", CaseConverter.convert("myVariableName", CaseStyle.SNAKE))
        assertEquals("xml_http_request", CaseConverter.convert("XMLHttpRequest", CaseStyle.SNAKE))
    }

    fun testConvertToKebabCase() {
        assertEquals("my-variable-name", CaseConverter.convert("myVariableName", CaseStyle.KEBAB))
    }

    fun testConvertToConstantCase() {
        assertEquals("MY_VARIABLE_NAME", CaseConverter.convert("myVariableName", CaseStyle.CONSTANT))
        assertEquals("MY_VARIABLE_NAME", CaseConverter.convert("my-variable-name", CaseStyle.CONSTANT))
    }

    fun testRoundTripFromEveryStyleProducesTheSameWords() {
        val variants = listOf("myVariableName", "MyVariableName", "my_variable_name", "my-variable-name", "MY_VARIABLE_NAME")
        val expectedWords = listOf("my", "variable", "name")
        for (variant in variants) {
            assertEquals("failed for input '$variant'", expectedWords, CaseConverter.words(variant).map { it.lowercase() })
        }
    }

    fun testConvertingToItsOwnStyleIsANoOpOnWords() {
        val camel = CaseConverter.convert("my_variable_name", CaseStyle.CAMEL)
        assertEquals(camel, CaseConverter.convert(camel, CaseStyle.CAMEL))
    }

    fun testEmptyInputReturnsItselfUnchanged() {
        assertEquals("", CaseConverter.convert("", CaseStyle.CAMEL))
    }

    fun testSingleWordConvertsCleanly() {
        assertEquals("total", CaseConverter.convert("TOTAL", CaseStyle.CAMEL))
        assertEquals("Total", CaseConverter.convert("total", CaseStyle.PASCAL))
    }
}
