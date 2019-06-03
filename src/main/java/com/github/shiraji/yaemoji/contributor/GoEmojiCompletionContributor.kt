package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.goide.psi.GoStringLiteral
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import org.jetbrains.plugins.ruby.ruby.lang.psi.basicTypes.stringLiterals.RExpressionSubstitution
import org.jetbrains.plugins.ruby.ruby.lang.psi.basicTypes.stringLiterals.RStringLiteral

class GoEmojiCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(GoStringLiteral::class.java),
                provider)
    }
}
