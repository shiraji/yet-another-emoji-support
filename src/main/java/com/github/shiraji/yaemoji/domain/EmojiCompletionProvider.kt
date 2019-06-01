package com.github.shiraji.yaemoji.domain

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.application.ex.ApplicationUtil.runWithCheckCanceled
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.util.ProcessingContext
import java.util.concurrent.Callable

class EmojiCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val offset = parameters.editor.caretModel.currentCaret.offset
        val lineStartOffset = parameters.editor.caretModel.currentCaret.visualLineStart
        if (parameters.editor.isOneLineMode) return
        val text = parameters.editor.document.text
        var colonPosition = offset
        for (i in offset downTo lineStartOffset) {
            if (text[i] == ' ') return
            else if (text[i] == ':') {
                colonPosition = i
                break
            }
        }

        val callable = Callable {
            EmojiDataManager.emojiList.forEach {
                result.addElement(LookupElementBuilder.create(it.label)
                        .withIcon(it.icon)
                        .withInsertHandler { insertionContext, _ ->
                            val document = insertionContext.document
                            document.replaceString(colonPosition, insertionContext.tailOffset, it.emoji)
                        }
                        .withPresentableText("${it.label} ${it.emoji}")
                )
            }
        }

        runWithCheckCanceled(callable, EmptyProgressIndicator())
    }
}