package dev.gaphunter.changecasecompanion.find

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange

/**
 * Works on the Editor's own text directly, never PSI -- case
 * conversion is a plain-text operation, and restricting it to
 * PSI-resolvable identifiers would make it useless on JSON/YAML keys,
 * CSS classes, or anywhere else an "identifier" isn't a real language
 * symbol. If there's a real selection, that wins outright (respects
 * exactly what the user selected, even if it spans multiple words/
 * punctuation the caret-expansion heuristic below wouldn't). With no
 * selection, expands from the caret to the nearest run of
 * letters/digits/`_`/`-` -- never guesses further than that.
 */
object SelectionResolver {

    fun resolve(editor: Editor): TextRange? {
        val selectionModel = editor.selectionModel
        if (selectionModel.hasSelection()) {
            return TextRange(selectionModel.selectionStart, selectionModel.selectionEnd)
        }

        val text = editor.document.charsSequence
        val offset = editor.caretModel.offset
        if (text.isEmpty()) return null

        fun isWordChar(c: Char) = c.isLetterOrDigit() || c == '_' || c == '-'

        // caret can sit one-past-the-end of the word it's "inside" (e.g. right after
        // the last letter) -- check the char just behind it too before giving up.
        if (offset < text.length && !isWordChar(text[offset]) && (offset == 0 || !isWordChar(text[offset - 1]))) {
            return null
        }

        var start = offset
        var end = offset
        while (start > 0 && isWordChar(text[start - 1])) start--
        while (end < text.length && isWordChar(text[end])) end++
        if (start == end) return null
        return TextRange(start, end)
    }
}
