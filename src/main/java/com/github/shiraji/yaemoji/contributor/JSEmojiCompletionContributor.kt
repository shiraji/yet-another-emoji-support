package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns

class JSEmojiCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC,
                // For now, let's ignore code inside JSStringTemplateExpression => ` $( foo ) ` because there is no class for $( foo )
                PlatformPatterns.psiElement().inside(JSLiteralExpression::class.java),
                provider)
    }
}
