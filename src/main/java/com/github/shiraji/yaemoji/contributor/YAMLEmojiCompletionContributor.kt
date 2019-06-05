package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.utils.or
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLQuotedText
import org.jetbrains.yaml.psi.YAMLValue

class YAMLEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(YAMLQuotedText::class.java)
            .or(PlatformPatterns.psiElement().inside(YAMLValue::class.java)) as PsiElementPattern.Capture<PsiElement>
}
