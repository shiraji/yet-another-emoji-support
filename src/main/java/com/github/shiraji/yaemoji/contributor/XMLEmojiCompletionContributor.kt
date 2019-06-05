package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.utils.or
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlText
import com.intellij.psi.xml.XmlToken

class XMLEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(XmlText::class.java)
        .or(PlatformPatterns.psiElement().inside(XmlToken::class.java))
        .or(PlatformPatterns.psiElement().inside(XmlComment::class.java))

    init {
        extend(CompletionType.BASIC, place, provider)
    }
}
