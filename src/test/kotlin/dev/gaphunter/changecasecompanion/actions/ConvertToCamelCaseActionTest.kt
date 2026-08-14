package dev.gaphunter.changecasecompanion.actions

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.testFramework.TestActionEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Exercises the REAL `update()`/`actionPerformed()` path through a
 * constructed [com.intellij.openapi.actionSystem.DataContext] --
 * same technique as every other Gap Hunter Labs plugin built the
 * night SDK_GOTCHAS.md §17 was written, specifically to catch a
 * disabled-action bug WITHOUT needing a live `runIde` sandbox. All 5
 * concrete actions share [ChangeCaseActionBase]'s logic, so testing
 * one concrete subclass end to end covers the shared code path for
 * all 5 -- [CaseConverterTest] already covers the per-style rendering
 * differences directly.
 */
class ConvertToCamelCaseActionTest : BasePlatformTestCase() {

    private fun dataContextFromFixture() = SimpleDataContext.builder()
        .add(CommonDataKeys.PROJECT, project)
        .add(CommonDataKeys.EDITOR, myFixture.editor)
        .build()

    fun testActionIsEnabledWhenThereIsAWordUnderTheCaret() {
        myFixture.configureByText("a.txt", "hello my_var<caret>iable world")
        val action = ConvertToCamelCaseAction()
        val event = TestActionEvent.createTestEvent(action, dataContextFromFixture())
        action.update(event)

        assertTrue(event.presentation.isEnabledAndVisible)
    }

    fun testActionIsDisabledWithNoWordAtTheCaret() {
        myFixture.configureByText("a.txt", "hello, <caret> world")
        val action = ConvertToCamelCaseAction()
        val event = TestActionEvent.createTestEvent(action, dataContextFromFixture())
        action.update(event)

        assertFalse(event.presentation.isEnabledAndVisible)
    }

    fun testActionIsDisabledWithNoEditorInTheDataContext() {
        val dataContext = SimpleDataContext.builder().add(CommonDataKeys.PROJECT, project).build()
        val action = ConvertToCamelCaseAction()
        val event = TestActionEvent.createTestEvent(action, dataContext)
        action.update(event)

        assertFalse(event.presentation.isEnabledAndVisible)
    }

    fun testActionPerformedRewritesTheRealDocument() {
        myFixture.configureByText("a.txt", "hello my_var<caret>iable world")
        val action = ConvertToCamelCaseAction()
        val event = TestActionEvent.createTestEvent(action, dataContextFromFixture())

        action.actionPerformed(event)

        assertEquals("hello myVariable world", myFixture.editor.document.text)
    }
}
