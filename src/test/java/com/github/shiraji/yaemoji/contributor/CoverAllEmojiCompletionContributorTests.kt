package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletion
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.testFramework.builders.ModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.plugins.scala.ScalaFileType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class CoverAllEmojiCompletionContributorTests : CodeInsightFixtureTestCase<ModuleFixtureBuilder<*>>() {

    @BeforeEach
    fun beforeEach() {
        setUp()

        val line = "548\t0x1F996\tT-Rex\tT-Rex | Tyrannosaurus Rex"
        val completion = EmojiCompletion.fromCsv(line)
        EmojiDataManager.emojiList.add(completion)
    }

    @AfterEach
    fun afterEach() {
        tearDown()
    }

    class SuccessArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // I'm not quite sure but if I put ":T<caret>" here, it fails show completion in some languages.
                // Because it is the problem with IntelliJ, This test case only see the case of ":<caret>"

                // Add test cases that does not work with AllEmojiCompletionContributorTests
                arguments(
                    GroovyFileType.GROOVY_FILE_TYPE, """
                    def text = "aaa :<caret> vvv"
                """.trimIndent()
                ),
                arguments(
                    JavaFileType.INSTANCE, """
                    class Foo {
                        String text = "aaa :<caret> ccc";
                    }""".trimIndent()
                ),
                arguments(JavaScriptFileType.INSTANCE, "'aaa :<caret> bbb'"),
                arguments(JavaScriptFileType.INSTANCE, "\"aaa :<caret> bbb\""),
                // I'm not sure why
                // arguments(JavaScriptFileType.INSTANCE, "`aaa :<caret> bbb`"),
//                arguments(
//                    PhpFileType.INSTANCE, """
//                    <?php
//                        'aaa :<caret> ddd';
//                    ?>
//                """.trimIndent()
//                ),
                arguments(
                    ScalaFileType.INSTANCE, """
                    val text = "aaa :<caret> ddd"
                """.trimIndent()
                ),
                arguments(
                    PropertiesFileType.INSTANCE, """
                        foo=aaa:T-Rex<caret>bbb
                    """.trimIndent()
                )
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(SuccessArgumentsProvider::class)
    fun `Should successfully show completion`(fileType: FileType, text: String) {
        myFixture.configureByText(fileType, text)
        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings
        assertThat(lookupStrings).contains(":T-Rex: 🦖 (:Tyrannosaurus Rex:)")
    }
}