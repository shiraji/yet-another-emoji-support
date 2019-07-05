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
        fun `Should do nothing if text contains white space`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":aaa bbb"
            every { parameters.editor.document.text } returns text
            every { parameters.editor.caretModel.currentCaret.offset - 1 } returns text.length
            every { parameters.position.textRange.startOffset - 1 } returns 0

            target.addCompletionVariants(parameters, context, result)
        }

        @Test
        fun `Should do nothing if no colon found`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = "aaabbb"
            every { parameters.editor.document.text } returns text
            every { parameters.editor.caretModel.currentCaret.offset - 1 } returns text.length
            every { parameters.position.textRange.startOffset - 1 } returns 0

            target.addCompletionVariants(parameters, context, result)
        }

        @Test
        fun `Should do nothing if start is smaller than end`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":aaabbb"
            every { parameters.editor.document.text } returns text
            every { parameters.editor.caretModel.currentCaret.offset - 1 } returns -1
            every { parameters.position.textRange.startOffset - 1 } returns 0

            target.addCompletionVariants(parameters, context, result)
        }

        @Test
        fun `Should add element if text is target with empty keyword`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":T-Rex"
            every { parameters.editor.document.text } returns text
            every { parameters.editor.caretModel.currentCaret.offset - 1 } returns text.length
            every { parameters.position.textRange.startOffset - 1 } returns 0
            val lookup = slot<LookupElement>()
            every { result.addElement(capture(lookup)) } just Runs

            EmojiDataManager.emojiList.add(EmojiCompletion(1, "T-Rex", "🦖", emptyList(), null))

            target.addCompletionVariants(parameters, context, result)

            assertThat(lookup.captured.lookupString).isEqualTo("T-Rex ")
        }

        @Test
        fun `Should add element if text is target with a keyword`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":T-Rex"
            every { parameters.editor.document.text } returns text
            every { parameters.editor.caretModel.currentCaret.offset - 1 } returns text.length
            every { parameters.position.textRange.startOffset - 1 } returns 0
            val lookup = slot<LookupElement>()
            every { result.addElement(capture(lookup)) } just Runs

            EmojiDataManager.emojiList.add(EmojiCompletion(1, "T-Rex", "🦖", listOf("Foo"), null))

            target.addCompletionVariants(parameters, context, result)

            assertThat(lookup.captured.lookupString).isEqualTo("T-Rex (Foo)")
        }

        @Test
        fun `Should add element if text is target with multiple keywords`() {
            every { parameters.editor.isOneLineMode } returns false
            val text = ":T-Rex"
            every { parameters.editor.document.text } returns text
            every { parameters.editor.caretModel.currentCaret.offset - 1 } returns text.length
            every { parameters.position.textRange.startOffset - 1 } returns 0
            val lookup = slot<LookupElement>()
            every { result.addElement(capture(lookup)) } just Runs

            EmojiDataManager.emojiList.add(EmojiCompletion(1, "T-Rex", "🦖", listOf("Foo", "Bar"), null))

            target.addCompletionVariants(parameters, context, result)

            assertThat(lookup.captured.lookupString).isEqualTo("T-Rex (Foo, Bar)")
        }
    }
}