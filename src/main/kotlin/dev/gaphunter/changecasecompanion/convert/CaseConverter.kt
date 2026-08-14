package dev.gaphunter.changecasecompanion.convert

import dev.gaphunter.changecasecompanion.model.CaseStyle

/**
 * Pure text transformation, no PSI, no Editor -- fully unit-testable
 * in isolation. Works starting from ANY of the 5 supported styles
 * (not just camelCase), because it never assumes the input's current
 * style: it first splits on separators (`_`, `-`, whitespace), then
 * further splits each chunk on camelCase/PascalCase word boundaries
 * -- including acronym runs (`XMLHttpRequest` -> `XML`, `Http`,
 * `Request`, not letter-by-letter) -- and only THEN re-assembles the
 * words into the target style. A no-op round trip (converting
 * `snake_case` text to `snake_case`) is expected to return the same
 * words, verified by a dedicated test.
 */
object CaseConverter {

    // 1: a run of 2+ uppercase letters immediately followed by an uppercase+lowercase pair
    //    (the run up to but not including that last uppercase belongs to the acronym: "XMLHttp" -> "XML" + "Http")
    // 2: an optional single uppercase letter followed by one or more lowercase/digits ("Http", "http2")
    // 3: a run of uppercase letters with no following lowercase (a whole-word acronym or CONSTANT chunk: "XML", "ID")
    // 4: a run of digits on their own
    private val WORD_BOUNDARY = Regex("[A-Z]+(?=[A-Z][a-z])|[A-Z]?[a-z0-9]+|[A-Z]+")

    fun words(text: String): List<String> {
        val separatorSplit = text.split(Regex("[_\\-\\s]+")).filter { it.isNotEmpty() }
        return separatorSplit.flatMap { chunk -> WORD_BOUNDARY.findAll(chunk).map { it.value }.toList() }
            .filter { it.isNotEmpty() }
    }

    fun convert(text: String, target: CaseStyle): String {
        val words = words(text)
        if (words.isEmpty()) return text
        return when (target) {
            CaseStyle.CAMEL -> words.first().lowercase() + words.drop(1).joinToString("") { capitalize(it) }
            CaseStyle.PASCAL -> words.joinToString("") { capitalize(it) }
            CaseStyle.SNAKE -> words.joinToString("_") { it.lowercase() }
            CaseStyle.KEBAB -> words.joinToString("-") { it.lowercase() }
            CaseStyle.CONSTANT -> words.joinToString("_") { it.uppercase() }
        }
    }

    private fun capitalize(word: String): String =
        word.lowercase().replaceFirstChar { it.uppercaseChar() }
}
