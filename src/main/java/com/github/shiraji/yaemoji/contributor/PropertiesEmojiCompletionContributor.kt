package com.github.shiraji.yaemoji.contributor

import com.intellij.lang.properties.psi.impl.PropertyValueImpl
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement

class PropertiesEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place: PsiElementPattern.Capture<out PsiElement> =
        PlatformPatterns.psiElement(PropertyValueImpl::class.java)
            // Not sure why but aaa:<caret>=bbb become PropertyValueImpl. No way to exclude this pattern 😭
            // .andNot(PlatformPatterns.psiElement().inside(PropertyKeyImpl::class.java))
}