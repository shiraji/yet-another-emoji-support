package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

class GroovyEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement().inside(GrLiteral::class.java)
}
