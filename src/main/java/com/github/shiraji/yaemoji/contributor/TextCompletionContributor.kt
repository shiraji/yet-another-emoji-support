package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiPlainText

class TextCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiPlainText::class.java), provider)
    }
}
