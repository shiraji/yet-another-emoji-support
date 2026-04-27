package com.github.shiraji.yaemoji.action

import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EmojiProjectActivityTest {

    private lateinit var fixture: CodeInsightTestFixture

    @BeforeEach
    fun beforeEach() {
        val fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory()
        val projectFixture = fixtureFactory.createLightFixtureBuilder("EmojiProjectActivityTest").fixture
        fixture = fixtureFactory.createCodeInsightFixture(projectFixture)
        fixture.setUp()
    }

    @AfterEach
    fun afterEach() {
        if (::fixture.isInitialized) {
            fixture.tearDown()
        }
    }

    @Test
    fun `Should load emojis`() {
        runBlocking {
            EmojiProjectActivity().execute(fixture.project)
        }

        assertTrue(EmojiDataManager.emojiList.size > 1000)
        assertTrue(EmojiDataManager.emojiList.any { it.id == 548 })
    }
}
