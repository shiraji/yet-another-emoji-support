package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement

class CommentCompletionContributor : EmojiCompletionContributor() {
    override val place: ElementPattern<out PsiElement> = PlatformPatterns.psiElement(PsiComment::class.java)
}
