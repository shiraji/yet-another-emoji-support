package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.action.EmojiProjectActivity
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.TestFixtureBuilder
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.plugins.ruby.ruby.lang.RubyFileType
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

class CoverAllEmojiCompletionContributorTests {

    private lateinit var myFixture: CodeInsightTestFixture

    @BeforeEach
    fun beforeEach() {

        val factory = IdeaTestFixtureFactory.getFixtureFactory()
        val fixtureBuilder: TestFixtureBuilder<IdeaProjectTestFixture> = factory.createLightFixtureBuilder("CoverAllEmojiCompletionContributorTests")
        val fixture = fixtureBuilder.fixture
        myFixture = IdeaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(fixture)
        myFixture.setUp()

        runBlocking {
            EmojiProjectActivity().execute(myFixture.project)
        }
    }

    @AfterEach
    fun afterEach() {
//        myFixture.tearDown()
    }

    class SuccessArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // I'm not quite sure but if I put ":T<caret>" here, it fails show completion in some languages.
                // Because it is the problem with IntelliJ, This test case only see the case of ":<caret>"

                // Add test cases that does not work with AllEmojiCompletionContributorTests
                arguments(
                    GroovyFileType.GROOVY_FILE_TYPE, """
                    def text = "aaa :T<caret> vvv"
                """.trimIndent(),
                    "Groovy string middle"
                ),
                arguments(
                    JavaFileType.INSTANCE, """
                    class Foo {
                        String text = "aaa :<caret> ccc";
                    }""".trimIndent(),
                    "Java string middle"
                ),
                arguments(JavaScriptFileType.INSTANCE, "'aaa :<caret> bbb'", "JS string middle"),
                arguments(JavaScriptFileType.INSTANCE, "\"aaa :<caret> bbb\"", "JS string middle2"),
                arguments(JavaScriptFileType.INSTANCE, "`aaa :<caret> bbb`", "JS string template middle"),
                // I'm not sure why
//                arguments(
//                    PhpFileType.INSTANCE, """
//                    <?php
//                        'aaa :<caret> ddd';
//                    ?>
//                """.trimIndent(),
//                    "PHP string middle"
//                ),
                arguments(
                    ScalaFileType.INSTANCE, """
                    val text = "aaa :<caret> ddd"
                """.trimIndent(),
                    "Scala string middle"
                ),
                arguments(
                    PropertiesFileType.INSTANCE, """
                        foo=aaa:T-Rex<caret>bbb
                    """.trimIndent(),
                    "Properties middle"
                ),
                // I'm not sure why
//                arguments(
//                    PropertiesFileType.INSTANCE, """
//                        foo=:T-R<caret>
//                    """.trimIndent(),
//                    "Properties value"
//                ),
//                arguments(
//                    PropertiesFileType.INSTANCE, """
//                        # :T-Rex<caret>
//                    """.trimIndent(),
//                    "Properties comment"
//                ),
//                arguments(
//                    CssFileType.INSTANCE, """
//                        /* :T-Rex<caret> */
//                    """.trimIndent(),
//                    "CSS comment"
//                ),
                arguments(
                    RubyFileType.RUBY, """
                        text = "aaa :<caret> bbb"
                    """.trimIndent(),
                    "Ruby string middle"
                )
            )
        }
    }

    @ParameterizedTest(name = "{2}")
    @ArgumentsSource(SuccessArgumentsProvider::class)
    fun `Should successfully show completion`(fileType: FileType, text: String, name: String) {
        myFixture.configureByText(fileType, text)
        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings
        assertThat(lookupStrings).isNotEmpty.contains(":T-Rex: 🦖 (:Tyrannosaurus Rex:)")
    }
}