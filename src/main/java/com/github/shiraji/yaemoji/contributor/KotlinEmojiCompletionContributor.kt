package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import org.jetbrains.kotlin.psi.KtBlockStringTemplateEntry
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

class KotlinEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(KtStringTemplateExpression::class.java)
            .andNot(PlatformPatterns.psiElement().inside(KtBlockStringTemplateEntry::class.java))
}
