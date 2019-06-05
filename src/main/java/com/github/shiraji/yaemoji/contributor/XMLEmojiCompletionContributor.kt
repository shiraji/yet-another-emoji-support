package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.utils.or
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlText
import com.intellij.psi.xml.XmlToken

class XMLEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place =
            PlatformPatterns.psiElement().inside(XmlText::class.java)
                    .or(PlatformPatterns.psiElement().inside(XmlToken::class.java))
                    .or(PlatformPatterns.psiElement().inside(XmlComment::class.java)) as PsiElementPattern.Capture<PsiElement>
}
