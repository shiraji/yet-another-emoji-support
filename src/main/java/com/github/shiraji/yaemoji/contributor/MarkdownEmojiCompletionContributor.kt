package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.github.shiraji.yaemoji.utils.or
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile
import org.jetbrains.yaml.psi.YAMLQuotedText
import org.jetbrains.yaml.psi.YAMLValue

class MarkdownEmojiCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(MarkdownFile::class.java).
                        andNot(PlatformPatterns.psiElement().inside(MarkdownCodeFenceImpl::class.java)),
                provider)
    }
}
