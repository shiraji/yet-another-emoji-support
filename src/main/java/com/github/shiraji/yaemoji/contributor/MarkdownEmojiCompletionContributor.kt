package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownCodeFence
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile

class MarkdownEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(MarkdownFile::class.java)
            .andNot(PlatformPatterns.psiElement().inside(MarkdownCodeFence::class.java))
}
