package com.github.shiraji.yaemoji.action

import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.testFramework.builders.ModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EmojiPreloadingActivityTest : CodeInsightFixtureTestCase<ModuleFixtureBuilder<*>>() {

    @BeforeEach
    fun beforeEach() {
        setUp()
        EmojiDataManager.emojiList = emptyList()
    }

    @AfterEach
    fun afterEach() {
        EmojiDataManager.emojiList = emptyList()
        tearDown()
    }

    @Test
    fun `Should load emojis`() {
        EmojiPreloadingActivity().preload(EmptyProgressIndicator())

        assertThat(EmojiDataManager.emojiList).hasSize(1719)
        assertThat(EmojiDataManager.emojiList[100].id).isEqualTo(101)
    }
}
