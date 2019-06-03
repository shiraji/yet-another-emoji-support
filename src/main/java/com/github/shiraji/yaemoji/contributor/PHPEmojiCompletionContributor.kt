package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.elements.Variable

class PHPEmojiCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(StringLiteralExpression::class.java)
                        .andNot(PlatformPatterns.psiElement().inside(Variable::class.java)),
                provider)
    }
}
