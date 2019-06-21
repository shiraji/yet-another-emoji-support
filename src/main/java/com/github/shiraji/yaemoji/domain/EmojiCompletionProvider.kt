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
        val end = maxOf(parameters.position.textRange.startOffset - 1, 0)
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
                index == end -> return
            }
        }

        if (colonPosition < 0) return

        val callable = Callable {
            EmojiDataManager.emojiList.forEach {
                val keywordsString = if (it.keywords.isEmpty()) "" else it.keywords.joinToString(prefix = "(", postfix = ")")
                result.addElement(LookupElementBuilder.create("${it.label} $keywordsString")
                        .withIcon(it.icon)
                        .withInsertHandler { insertionContext, _ ->
                            val document = insertionContext.document
                            document.replaceString(colonPosition, insertionContext.tailOffset, it.emoji)
                        }
                        .withPresentableText(":${it.label}: ${it.emoji} $keywordsString")
                )
            }
        }

        runWithCheckCanceled(callable, EmptyProgressIndicator())
    }
}