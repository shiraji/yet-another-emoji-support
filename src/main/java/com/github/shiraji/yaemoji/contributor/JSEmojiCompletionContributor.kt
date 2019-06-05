package com.github.shiraji.yaemoji.contributor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement

class JSEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement().inside(JSLiteralExpression::class.java)

    init {
        extend(CompletionType.BASIC, place, provider)
    }
}
