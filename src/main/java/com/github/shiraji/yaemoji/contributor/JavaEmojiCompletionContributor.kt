package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiJavaToken

class JavaEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiJavaToken> = PlatformPatterns.psiElement(PsiJavaToken::class.java)
}
