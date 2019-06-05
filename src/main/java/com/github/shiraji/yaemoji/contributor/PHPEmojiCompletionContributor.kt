package com.github.shiraji.yaemoji.contributor

import com.intellij.patterns.PlatformPatterns
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.elements.Variable

class PHPEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(StringLiteralExpression::class.java)
            .andNot(PlatformPatterns.psiElement().inside(Variable::class.java))
}
