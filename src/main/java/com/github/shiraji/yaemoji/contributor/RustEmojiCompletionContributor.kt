package com.github.shiraji.yaemoji.contributor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.rust.lang.core.psi.RsLitExpr

class RustEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement().inside(RsLitExpr::class.java)

    init {
        extend(CompletionType.BASIC, place, provider)
    }
}
