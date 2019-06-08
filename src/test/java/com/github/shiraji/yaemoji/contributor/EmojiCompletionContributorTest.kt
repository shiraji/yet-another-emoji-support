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
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Nested

class EmojiCompletionContributorTest {

    private val provider: CompletionProvider<CompletionParameters> = mockk()
    private val mockPlace: PsiElementPattern.Capture<out PsiElement> = mockk()
    private val target = object : EmojiCompletionContributor(provider) {
        override val place = mockPlace
    }

    @Nested
    inner class fillCompletionVariants {
        @Test
        fun `Should do nothing if completionType is not BASIC`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.CLASS_NAME
            val result: CompletionResultSet = mockk()

            target.fillCompletionVariants(parameters, result)

            verify(exactly = 0) { provider.addCompletionVariants(any(), any(), any()) }
        }

        @Test
        fun `Should do nothing if place is not accepts`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns false
            val result: CompletionResultSet = mockk()

            target.fillCompletionVariants(parameters, result)

            verify(exactly = 0) { provider.addCompletionVariants(any(), any(), any()) }
        }

        @Test
        fun `Should do nothing if prefix does not contains semi colon`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns true
            val result: CompletionResultSet = mockk()
            every { result.prefixMatcher.prefix } returns "asdfajsd"

            target.fillCompletionVariants(parameters, result)

            verify(exactly = 0) { provider.addCompletionVariants(any(), any(), any()) }
        }

        @Test
        fun `Should do nothing if prefix contains semi colon but last segment contains space`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns true
            val result: CompletionResultSet = mockk()
            every { result.prefixMatcher.prefix } returns "asdfajsd:aa a"

            target.fillCompletionVariants(parameters, result)

            verify(exactly = 0) { provider.addCompletionVariants(any(), any(), any()) }
        }

        @Test
        fun `Should addCompletionVariants if prefix is valid`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns true
            val result: CompletionResultSet = mockk()
            every { result.prefixMatcher.prefix } returns "asdfajsd:aa"
            val slot = slot<String>()
            every { result.withPrefixMatcher(capture(slot)) } returns mockk()
            every { provider.addCompletionVariants(any(), any(), any()) } just Runs

            target.fillCompletionVariants(parameters, result)

            assertEquals("aa", slot.captured)
        }

        @Test
        fun `Should addCompletionVariants if prefix is valid with space other than last segement`() {
            val parameters: CompletionParameters = mockk()
            every { parameters.completionType } returns CompletionType.BASIC
            every { parameters.position } returns mockk()
            every { mockPlace.accepts(any()) } returns true
            val result: CompletionResultSet = mockk()
            every { result.prefixMatcher.prefix } returns "asdfajs d:"
            val slot = slot<String>()
            every { result.withPrefixMatcher(capture(slot)) } returns mockk()
            every { provider.addCompletionVariants(any(), any(), any()) } just Runs

            target.fillCompletionVariants(parameters, result)

            assertEquals("", slot.captured)
        }
    }
}