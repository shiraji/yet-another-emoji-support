package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlText
import com.intellij.psi.xml.XmlToken
import org.jetbrains.kotlin.idea.completion.or
import org.jetbrains.yaml.psi.YAMLQuotedText
import org.jetbrains.yaml.psi.YAMLValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class YAMLEmojiCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(YAMLQuotedText::class.java)
                        .or(PlatformPatterns.psiElement().inside(YAMLValue::class.java)),
                provider)
    }
}
