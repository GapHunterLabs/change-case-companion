package dev.gaphunter.changecasecompanion.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import dev.gaphunter.changecasecompanion.convert.CaseConverter
import dev.gaphunter.changecasecompanion.find.SelectionResolver
import dev.gaphunter.changecasecompanion.model.CaseStyle
import dev.gaphunter.changecasecompanion.review.ReviewPrompt

/**
 * Shared implementation for all 5 "Convert to &lt;style&gt;" actions --
 * the only thing that differs between them is [targetCase], set by
 * each concrete subclass's own no-arg constructor (plugin.xml
 * instantiates actions via a no-arg constructor, so this can't be one
 * parametrized class registered 5 times).
 *
 * Deliberately synchronous on the EDT, unlike this catalog's PSI-
 * heavy plugins -- splitting and rejoining a short identifier string
 * is microseconds of work, not the kind of computation that needs
 * moving off-EDT. Forcing a pooled-thread hop here would add real
 * complexity (and real test-flakiness risk -- see Turbo Log
 * Companion's own lesson the same night) for zero benefit.
 */
abstract class ChangeCaseActionBase(private val targetCase: CaseStyle) : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = e.project != null && editor != null && SelectionResolver.resolve(editor) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val range = SelectionResolver.resolve(editor) ?: return

        val original = editor.document.getText(range)
        val converted = CaseConverter.convert(original, targetCase)
        if (converted == original) {
            notify(project, "Already ${targetCase.displayName}.")
            return
        }

        WriteCommandAction.runWriteCommandAction(project, "Convert to ${targetCase.displayName}", null, {
            editor.document.replaceString(range.startOffset, range.endOffset, converted)
        })
        notify(project, "Converted to ${targetCase.displayName}.")
        // Real conversion only -- never counted for the "Already X" no-op branch above.
        ReviewPrompt.recordHit(project)
    }

    private fun notify(project: Project, message: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Change Case Companion")
            .createNotification(message, NotificationType.INFORMATION)
            .notify(project)
    }
}
