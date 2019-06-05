package com.github.shiraji.yaemoji.contributor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiPlainText

class TextCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiPlainText> = PlatformPatterns.psiElement(PsiPlainText::class.java)

    init {
        extend(CompletionType.BASIC, place, provider)
    }
}
