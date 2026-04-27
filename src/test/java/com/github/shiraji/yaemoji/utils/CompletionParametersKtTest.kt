package com.github.shiraji.yaemoji.utils

import com.intellij.codeInsight.completion.CompletionParameters
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CompletionParametersKtTest {

    @Nested
    inner class findColonPosition {

        private val parameters: CompletionParameters = mockk()

        @Test
        fun `Should get first colon`() {
            val text = ":what"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text

            val result = parameters.findColonPosition()

            assertEquals(0, result)
        }

        @Test
        fun `Should get first matching colon`() {
            val text = "::what"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text

            val result = parameters.findColonPosition()

            assertEquals(1, result)
        }

        @Test
        fun `Should get -1 if space before colon`() {
            val text = ": what"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text

            val result = parameters.findColonPosition()

            assertEquals(-1, result)
        }

        @Test
        fun `Should get -1 if no colon`() {
            val text = "what"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 0
            every { parameters.editor.document.text } returns text

            val result = parameters.findColonPosition()

            assertEquals(-1, result)
        }

        @Test
        fun `Should get -1 if end before colon`() {
            val text = ":what"
            every { parameters.editor.caretModel.currentCaret.offset } returns text.length
            every { parameters.position.textRange.startOffset } returns 2
            every { parameters.editor.document.text } returns text

            val result = parameters.findColonPosition()

            assertEquals(-1, result)
        }
    }
}