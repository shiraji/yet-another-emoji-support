package com.github.shiraji.yaemoji.domain

import com.github.shiraji.emoji.service.EmojiReader
import com.github.shiraji.yaemoji.utils.findColonPosition
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class EmojiCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        if (parameters.editor.isOneLineMode) return
        val colonPosition = parameters.findColonPosition()
        if (colonPosition < 0) return

        if (EmojiDataManager.emojiList.isEmpty()) {
            EmojiDataManager.emojiList.addAll(EmojiReader.loadEmoji())
        }

        EmojiDataManager.emojiList.forEach {
            val keywords = it.keywords.map { keyword -> ":$keyword:" }
            val keywordsString = if (keywords.isEmpty()) "" else keywords.joinToString(prefix = "(", postfix = ")")
            result.addElement(LookupElementBuilder.create(":${it.label}: ${it.emoji} $keywordsString")
                .withIcon(it.icon)
                .also { builder ->
                    keywords.forEach { keyword ->
                        builder.withLookupString(keyword)
                    }
                }
                .withInsertHandler { insertionContext, _ ->
                    val document = insertionContext.document
                    document.replaceString(colonPosition, insertionContext.tailOffset, it.emoji)
                }
            )
        }
    }
}