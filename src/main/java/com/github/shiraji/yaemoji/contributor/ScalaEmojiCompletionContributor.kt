package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.scala.lang.psi.api.base.ScLiteral
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScInfixExpr

class ScalaEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement().inside(ScLiteral::class.java)
        .andNot(PlatformPatterns.psiElement().inside(ScInfixExpr::class.java))
}
