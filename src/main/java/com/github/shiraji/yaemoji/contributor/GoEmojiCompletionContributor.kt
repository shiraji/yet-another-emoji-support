package com.github.shiraji.yaemoji.contributor

import com.goide.psi.GoStringLiteral
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement

class GoEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement().inside(GoStringLiteral::class.java)
}
