package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.ruby.ruby.lang.psi.basicTypes.stringLiterals.RExpressionSubstitution
import org.jetbrains.plugins.ruby.ruby.lang.psi.basicTypes.stringLiterals.RStringLiteral

class RubyEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement().inside(RStringLiteral::class.java)
            .andNot(PlatformPatterns.psiElement().inside(RExpressionSubstitution::class.java))
}
