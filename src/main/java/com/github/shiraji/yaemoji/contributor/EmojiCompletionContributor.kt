package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.PrefixMatcher
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

abstract class EmojiCompletionContributor(
    protected val provider: CompletionProvider<CompletionParameters> = EmojiCompletionProvider()
) : CompletionContributor() {
    abstract val place: ElementPattern<out PsiElement>

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        if (parameters.completionType == CompletionType.BASIC && place.accepts(parameters.position)) {
            val prefix = result.prefixMatcher.toEmojiPrefix() ?: return
            provider.addCompletionVariants(parameters, ProcessingContext(), result.withPrefixMatcher(prefix))
        }
    }

    private fun PrefixMatcher.toEmojiPrefix(): String? {
        val currentPrefix = prefix
        if (!currentPrefix.contains(Regex(":"))) return null
        val newPrefix = currentPrefix.split(Regex(":")).last()
        return if (newPrefix.contains(Regex("\\s"))) null else newPrefix
    }
}