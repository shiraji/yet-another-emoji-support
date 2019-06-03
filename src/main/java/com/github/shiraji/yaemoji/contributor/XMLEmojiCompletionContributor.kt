package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlText
import com.intellij.psi.xml.XmlToken
import org.jetbrains.kotlin.idea.completion.or

class XMLEmojiCompletionContributor : CompletionContributor() {
    init {
        val provider = EmojiCompletionProvider()

        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(XmlText::class.java)
                        .or(PlatformPatterns.psiElement().inside(XmlToken::class.java))
                        .or(PlatformPatterns.psiElement().inside(XmlComment::class.java)),
                provider)
    }
}
