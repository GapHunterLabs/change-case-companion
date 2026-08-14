package dev.gaphunter.changecasecompanion.find

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class SelectionResolverTest : BasePlatformTestCase() {

    fun testUsesTheRealSelectionWhenOneExists() {
        myFixture.configureByText("a.txt", "hello <selection>my_variable</selection> world")
        val range = SelectionResolver.resolve(myFixture.editor)

        assertNotNull(range)
        assertEquals("my_variable", myFixture.editor.document.getText(range!!))
    }

    fun testExpandsFromCaretToTheWordWhenNoSelection() {
        myFixture.configureByText("a.txt", "hello my_var<caret>iable world")
        val range = SelectionResolver.resolve(myFixture.editor)

        assertNotNull(range)
        assertEquals("my_variable", myFixture.editor.document.getText(range!!))
    }

    fun testExpandsFromCaretRightAfterTheWord() {
        myFixture.configureByText("a.txt", "hello myVariable<caret> world")
        val range = SelectionResolver.resolve(myFixture.editor)

        assertNotNull(range)
        assertEquals("myVariable", myFixture.editor.document.getText(range!!))
    }

    fun testReturnsNullWhenCaretIsBetweenTwoNonWordCharacters() {
        myFixture.configureByText("a.txt", "hello, <caret> world")
        assertNull(SelectionResolver.resolve(myFixture.editor))
    }

    fun testKebabCaseCountsAsOneWordSpan() {
        myFixture.configureByText("a.txt", "hello my-var<caret>iable-name world")
        val range = SelectionResolver.resolve(myFixture.editor)

        assertNotNull(range)
        assertEquals("my-variable-name", myFixture.editor.document.getText(range!!))
    }
}
