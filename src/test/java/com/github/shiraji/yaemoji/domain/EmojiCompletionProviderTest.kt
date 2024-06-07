package com.github.shiraji.yaemoji.domain

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.util.ProcessingContext
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EmojiCompletionProviderTest {

    private val target = EmojiCompletionProvider()
    private val parameters: CompletionParameters = mockk()
    private val context: ProcessingContext = mockk()
    private val result: CompletionResultSet = mockk()

    @Nested
    inner class addCompletions {
        @Test
        fun `Should do nothing if editor is oneline mode`() {
            every { parameters.editor.isOneLineMode } returns true
            target.addCompletionVariants(parameters, context, result)
        }

        @Test
        fun `Should do nothing if no colon found`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = "what"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text

            target.addCompletionVariants(parameters, context, result)
        }

        @Test
        fun `Should add element`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":T-Rex"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text
            val lookup = slot<LookupElement>()
            every { result.addElement(capture(lookup)) } just Runs

            EmojiDataManager.loadEmoji()

            target.addCompletionVariants(parameters, context, result)

            assertThat(lookup.captured.lookupString).isEqualTo(":T-Rex: \uD83E\uDD96 (:Tyrannosaurus Rex:)")
        }

        @Test
        fun `Should not throw Exception if emojiList is empty`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":T-Rex"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text
            every { result.addElement(any()) } just Runs
            EmojiDataManager.clearEmoji()
            assertDoesNotThrow { target.addCompletionVariants(parameters, context, result) }
        }
    }
}