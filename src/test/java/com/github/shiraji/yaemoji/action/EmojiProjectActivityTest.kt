package com.github.shiraji.yaemoji.action

import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.testFramework.builders.ModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EmojiProjectActivityTest : CodeInsightFixtureTestCase<ModuleFixtureBuilder<*>>() {

    @BeforeEach
    fun beforeEach() {
        setUp()
    }

    @AfterEach
    fun afterEach() {
        tearDown()
    }

    @Test
    fun `Should load emojis`() {
        runBlocking {
            EmojiProjectActivity().execute(project)
        }

        assertThat(EmojiDataManager.emojiList).hasSize(1)
        assertThat(EmojiDataManager.emojiList[0].id).isEqualTo(548)
    }
}
