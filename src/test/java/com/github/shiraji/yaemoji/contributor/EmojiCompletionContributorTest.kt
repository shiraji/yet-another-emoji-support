package com.github.shiraji.yaemoji.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EmojiCompletionContributorTest {

    private val provider: CompletionProvider<CompletionParameters> = mockk()
    private val mockPlace: PsiElementPattern.Capture<out PsiElement> = mockk()
    private val target = object : EmojiCompletionContributor(provider) {
        override val place = mockPlace
    }

    @Nested
    inner class fillCompletionVariants {

        @Test
        fun `Should do nothing if completion type is not basic`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.CLASS_NAME
            val result: CompletionResultSet = mockk()
            target.fillCompletionVariants(parameters, result)
            verify(exactly = 0) { provider.addCompletionVariants(any(), any(), any()) }
        }

        @Test
        fun `Should do nothing if accept returns false`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns false
            val result: CompletionResultSet = mockk()
            target.fillCompletionVariants(parameters, result)
            verify(exactly = 0) { provider.addCompletionVariants(any(), any(), any()) }
        }

        @Test
        fun `Should use new result if colon found`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns true
            every { parameters.editor.caretModel.currentCaret.offset } returns 5
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns ":what"
            every { parameters.editor.document.getText(any()) } returns ":what"
            val result: CompletionResultSet = mockk()
            val newResult: CompletionResultSet = mockk()
            every { result.withPrefixMatcher(any<String>()) } returns newResult
            val slot = slot<CompletionResultSet>()
            every { provider.addCompletionVariants(any(), any(), capture(slot)) } just Runs

            target.fillCompletionVariants(parameters, result)

            verify { result.withPrefixMatcher(any<String>()) }
            assertEquals(newResult, slot.captured)
        }

        @Test
        fun `Should use parameter result if colon not found`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns true
            every { parameters.editor.caretModel.currentCaret.offset } returns 4
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns "what"
            every { parameters.editor.document.getText(any()) } returns "what"
            val result: CompletionResultSet = mockk()
            val slot = slot<CompletionResultSet>()
            every { provider.addCompletionVariants(any(), any(), capture(slot)) } just Runs

            target.fillCompletionVariants(parameters, result)

            assertEquals(result, slot.captured)
        }
    }
}