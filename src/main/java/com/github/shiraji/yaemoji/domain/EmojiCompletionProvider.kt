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
        if (parameters.editor.isOneLineMode) return
        // -1 because it should check with text's index
        val start = parameters.editor.caretModel.currentCaret.offset - 1
        // no -1 because downTo method is inclusive + the plugin need index of the text
        val end = parameters.editor.caretModel.currentCaret.visualLineStart
        val text = parameters.editor.document.text
        var colonPosition = -1
        loop@ for (index in start downTo end) {
            val current = text[index]
            when {
                current.isWhitespace() -> return
                current == ':' -> {
                    colonPosition = index
                    break@loop
                }
            }
        }

        if (colonPosition < 0) return

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