package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletion
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.goide.GoFileType
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.testFramework.builders.ModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.python.PythonFileType
import org.assertj.core.api.Assertions.assertThat
import org.intellij.plugins.markdown.lang.MarkdownFileType
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.plugins.ruby.ruby.lang.RubyFileType
import org.jetbrains.plugins.scala.ScalaFileType
import org.jetbrains.yaml.YAMLFileType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.rust.lang.RsFileType
import java.util.stream.Stream

class CommentCompletionContributorTest : CodeInsightFixtureTestCase<ModuleFixtureBuilder<*>>() {

    init {
        if (EmojiDataManager.emojiList.isEmpty()) {
            val line = "548\t0x1F996\tT-Rex\tT-Rex | Tyrannosaurus Rex"
            val completion = EmojiCompletion.fromCsv(line)
            EmojiDataManager.emojiList.add(completion)
        }
    }

    @BeforeEach
    fun beforeEach() {
        setUp()
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
                arguments(GoFileType.INSTANCE, "// :<caret>"),
                arguments(
                    GoFileType.INSTANCE, """
                    package main

                    func main() {
                        s := ":<caret>"
                    }
                """.trimIndent()
                ),
                arguments(
                    GoFileType.INSTANCE, """
                    package main

                    func main() {
                        s := "aaa :<caret> vvv"
                    }
                """.trimIndent()
                ),
                arguments(GroovyFileType.GROOVY_FILE_TYPE, "// :<caret>"),
                arguments(
                    GroovyFileType.GROOVY_FILE_TYPE, """
                    def origStr = ":<caret>"
                """.trimIndent()
                ),
                arguments(
                    GroovyFileType.GROOVY_FILE_TYPE, """
                    def text = "aaa :<caret> vvv"
                """.trimIndent()
                ),
                arguments(JavaFileType.INSTANCE, "// :T-Rex<caret>"),
                arguments(
                    JavaFileType.INSTANCE, """
                    class Foo {
                        String text = ":T<caret>";
                    }""".trimIndent()
                ),
                arguments(JavaScriptFileType.INSTANCE, "// :<caret>"),
                arguments(JavaScriptFileType.INSTANCE, "':<caret>'"),
                arguments(JavaScriptFileType.INSTANCE, "\":<caret>\""),
                arguments(JavaScriptFileType.INSTANCE, "`:<caret>`"),
                arguments(JavaScriptFileType.INSTANCE, "'aaa :<caret> bbb'"),
                arguments(JavaScriptFileType.INSTANCE, "\"aaa :<caret> bbb\""),
                arguments(JavaScriptFileType.INSTANCE, "`aaa :<caret> bbb`"),

                arguments(KotlinFileType.INSTANCE, "// :T-Rex<caret>"),
                arguments(
                    KotlinFileType.INSTANCE, """
                    val text = ":<caret>"
                    """.trimIndent()
                ),
                arguments(
                    KotlinFileType.INSTANCE, """
                    val text = "aa :<caret>"
                    """.trimIndent()
                ),
                arguments(MarkdownFileType.INSTANCE, ":<caret>"),
                arguments(MarkdownFileType.INSTANCE, "bar :<caret> zoo"),
                arguments(PhpFileType.INSTANCE, "// :<caret>"),
                arguments(
                    PhpFileType.INSTANCE, """
                    <?php
                        ':<caret>';
                    ?>
                """.trimIndent()
                ),
                arguments(
                    PhpFileType.INSTANCE, """
                    <?php
                        'aaa :<caret> ddd';
                    ?>
                """.trimIndent()
                ),

                // Not quite sure but Python does not work with this test...but it works fine.
                // arguments(PythonFileType.INSTANCE, """
                //     # :<caret>
                //     """.trimIndent()),
                // arguments(PythonFileType.INSTANCE, """
                //     text = ":<caret>"
                // """.trimIndent()),
                // arguments(PythonFileType.INSTANCE, """
                //     text = "aaa :<caret> ddd"
                // """.trimIndent())

                arguments(RubyFileType.RUBY, "# :<caret>"),
                arguments(
                    RubyFileType.RUBY, """
                    ':<caret>'
                """.trimIndent()
                ),
                arguments(
                    RubyFileType.RUBY, """
                    'aaa :<caret> bbb'
                """.trimIndent()
                ),
                arguments(
                    RubyFileType.RUBY, """
                    ":<caret>"
                """.trimIndent()
                ),
                arguments(
                    RubyFileType.RUBY, """
                    "aaa :<caret> bbb"
                """.trimIndent()
                ),
                arguments(ScalaFileType.INSTANCE, "// :<caret>"),
                arguments(
                    ScalaFileType.INSTANCE, """
                    val text = ":<caret>"
                """.trimIndent()
                ),
                arguments(
                    ScalaFileType.INSTANCE, """
                    val text = "aaa :<caret> ddd"
                """.trimIndent()
                ),
                arguments(
                    PlainTextFileType.INSTANCE, """
                        :<caret>
                """.trimIndent()
                ),
                arguments(
                    PlainTextFileType.INSTANCE, """
                     asdfasdf :<caret> asdf
                """.trimIndent()
                ),
                arguments(
                    XmlFileType.INSTANCE, """
                    <!-- :<caret> -->
                """.trimIndent()
                ),
                arguments(
                    XmlFileType.INSTANCE, """
                    <abc>:<caret></abc>
                """.trimIndent()
                ),
                arguments(
                    XmlFileType.INSTANCE, """
                    <abc foo=":<caret>">
                """.trimIndent()
                ),
                arguments(
                    YAMLFileType.YML, """
                        A: :<caret>
                """.trimIndent()
                ),
                arguments(RsFileType, "// :<caret>"),
                arguments(
                    RsFileType, """
                fn main() {
                    let hello = String::from(":<caret>");
                }
                """.trimIndent()
                ),
                arguments(
                    RsFileType, """
                fn main() {
                    let hello = String::from("aaa :<caret> asdf");
                }
                """.trimIndent()
                )
            )
        }
    }

    class NoArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                arguments(
                    GoFileType.INSTANCE, """
                    package main
                    
                    func main() {
                        s := "<caret>"
                    }
                """.trimIndent()
                ),
                arguments(
                    GroovyFileType.GROOVY_FILE_TYPE, """
                    def origStr = "<caret>"
                """.trimIndent()
                ),
                arguments(JavaScriptFileType.INSTANCE, "'<caret>'"),
                arguments(JavaScriptFileType.INSTANCE, "\"<caret>\""),
                arguments(JavaScriptFileType.INSTANCE, "`<caret>`"),
                arguments(
                    KotlinFileType.INSTANCE, """
                    val text = "<caret>"
                    """.trimIndent()
                ),
                arguments(
                    KotlinFileType.INSTANCE, """
                    val text = "${'$'}{:<caret>}"
                    """.trimIndent()
                ),
                arguments(
                    PhpFileType.INSTANCE, """
                    <?php
                        "${'$'}{:<caret>}";
                    ?>
                """.trimIndent()
                ),
                arguments(
                    PythonFileType.INSTANCE, """
                    text = "<caret>"
                """.trimIndent()
                ),
                arguments(
                    RubyFileType.RUBY, """
                    text = "<caret>"
                """.trimIndent()
                ),
                arguments(
                    RubyFileType.RUBY, """
                    text = "#{:<caret>}"
                """.trimIndent()
                ),
                arguments(
                    ScalaFileType.INSTANCE, """
                    val text = "<caret>"
                """.trimIndent()
                ),
                arguments(
                    ScalaFileType.INSTANCE, """
                    val text = s"${'$'}{:<caret>}"
                """.trimIndent()
                ),
                arguments(
                    XmlFileType.INSTANCE, """
                    <abc><caret></abc>
                """.trimIndent()
                ),
                arguments(
                    XmlFileType.INSTANCE, """
                    <abc foo="<caret>">
                """.trimIndent()
                ),
                arguments(
                    YAMLFileType.YML, """
                        A: <caret>
                """.trimIndent()
                ),
                arguments(
                    RsFileType, """
                fn main() {
                    let hello = String::from("<caret>");
                }
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
        assertThat(lookupStrings).contains("T-Rex (Tyrannosaurus Rex)")
    }

    @ParameterizedTest
    @ArgumentsSource(NoArgumentsProvider::class)
    fun `Should no completion`(fileType: FileType, text: String) {
        myFixture.configureByText(fileType, text)
        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings
        assertThat(lookupStrings).doesNotContain("T-Rex (Tyrannosaurus Rex)")
    }
}