package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.utils.or
import com.intellij.patterns.PlatformPatterns
import org.jetbrains.yaml.psi.YAMLQuotedText
import org.jetbrains.yaml.psi.YAMLValue

class YAMLEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(YAMLQuotedText::class.java)
            .or(PlatformPatterns.psiElement().inside(YAMLValue::class.java))
}
