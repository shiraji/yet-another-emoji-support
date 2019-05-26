package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletion
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.application.ex.ApplicationUtil.runWithCheckCanceled
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiComment
import com.intellij.util.ProcessingContext
import java.util.concurrent.Callable

class EmojiCompletionContributor : CompletionContributor() {

    init {
        val callable = Callable<List<EmojiCompletion>> {
            javaClass.getResourceAsStream("/emojis/emoji.csv").use {
                it.bufferedReader().use {
                    it.readLines().map {
                        EmojiCompletion.fromCsv(it)
                    }
                }
            }
        }

        val emojiList = runWithCheckCanceled(
            callable, EmptyProgressIndicator()
        )

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiComment::class.java), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
                val offset = parameters.editor.caretModel.currentCaret.offset
                val lineStartOffset = parameters.editor.caretModel.currentCaret.visualLineStart
                val text = parameters.editor.document.text
                var colonPosition = offset
                for (i in offset downTo lineStartOffset) {
                    if (text[i] == ' ') return
                    else if (text[i] == ':') {
                        colonPosition = i
                        break
                    }
                }

                emojiList.forEach {
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
        })

    }
}
