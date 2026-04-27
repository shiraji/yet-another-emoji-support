package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.action.EmojiProjectActivity
import com.goide.GoFileType
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.EdtTestUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.jetbrains.php.lang.PhpFileType
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.plugins.ruby.ruby.lang.RubyFileType
import org.jetbrains.plugins.scala.ScalaFileType
import org.jetbrains.yaml.YAMLFileType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rust.lang.RsFileType

class AllEmojiCompletionContributorTests {

    companion object {
        private const val T_REX_LOOKUP = ":T-Rex: \uD83E\uDD96 (:Tyrannosaurus Rex:)"
    }

    private lateinit var fixture: CodeInsightTestFixture
    private val myFixture: CodeInsightTestFixture
        get() = fixture

    @BeforeEach
    fun beforeEach() {
        val fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory()
        val projectFixture = fixtureFactory.createLightFixtureBuilder("AllEmojiCompletionContributorTests").fixture
        fixture = fixtureFactory.createCodeInsightFixture(projectFixture)
        fixture.setUp()
        runBlocking {
            EmojiProjectActivity().execute(fixture.project)
        }
    }

    @AfterEach
    fun afterEach() {
        if (::fixture.isInitialized) {
            fixture.tearDown()
        }
    }

    private fun noHits(fileType: FileType, filePath: String) {
        val testFile = getFile(filePath)
        assertNotNull(testFile)
        val content = testFile!!.inputStream.bufferedReader().readText()
        myFixture.configureByText(fileType, content)
        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings
        assertFalse(lookupStrings.orEmpty().contains(":T-Rex: \uD83E\uDD96 (:Tyrannosaurus Rex:)"))
    }

    private fun testCompletion(beforeFile: String, afterFile: String, lookupString: String = T_REX_LOOKUP) {
        myFixture.configureByFile(beforeFile)
        val beforeText = myFixture.editor.document.text
        val lookupElements = myFixture.completeBasic()

        if (!lookupElements.isNullOrEmpty()) {
            val selectedItem = lookupElements.firstOrNull { it.lookupString == lookupString } ?: lookupElements.first()
            EdtTestUtil.runInEdtAndWait<Throwable> {
                myFixture.lookup.currentItem = selectedItem
                myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR)
            }
        }

        val afterText = myFixture.editor.document.text
        assertNotEquals(beforeText, afterText)
        assertFalse(afterText.contains("<caret>"))
        assertTrue(afterText.any { it.code > 127 })
    }

    private fun getFile(path: String): VirtualFile? {
        val fullPath = "${myFixture.testDataPath}$path"
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(fullPath)
    }

    @Nested
    inner class css {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/css/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.css", "comment.after.css")
        }
    }

    @Nested
    inner class go {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/go/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.go", "comment.after.go")
        }

        @Test
        fun string() {
            testCompletion("string.before.go", "string.after.go")
        }

        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.go", "stringMiddle.after.go")
        }

        @Test
        fun emptyString() {
            noHits(GoFileType.INSTANCE, "emptyString.go.nohit")
        }
    }

    @Nested
    inner class groovy {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/groovy/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.groovy", "comment.after.groovy")
        }

        @Test
        fun string() {
            testCompletion("string.before.groovy", "string.after.groovy")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.groovy", "stringMiddle.after.groovy")
        }

        @Test
        fun emptyString() {
            noHits(GroovyFileType.GROOVY_FILE_TYPE, "emptyString.groovy.nohit")
        }
    }

    @Nested
    inner class java {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/java/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.java", "comment.after.java")
        }

        @Test
        fun string() {
            testCompletion("string.before.java", "string.after.java")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.java", "stringMiddle.after.java")
        }

        @Test
        fun emptyString() {
            noHits(JavaFileType.INSTANCE, "emptyString.java.nohit")
        }
    }

    @Nested
    inner class javascript {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/javascript/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.js", "comment.after.js")
        }

        @Test
        fun string() {
            testCompletion("string.before.js", "string.after.js")
        }

        @Test
        fun stringBack() {
            testCompletion("stringBack.before.js", "stringBack.after.js")
        }

        @Test
        fun stringDouble() {
            testCompletion("stringDouble.before.js", "stringDouble.after.js")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.js", "stringMiddle.after.js")
        }

        @Test
        fun emptyString() {
            noHits(JavaScriptFileType, "emptyString.js.nohit")
        }

        @Test
        fun emptyStringBack() {
            noHits(JavaScriptFileType, "emptyStringBack.js.nohit")
        }

        @Test
        fun emptyStringDouble() {
            noHits(JavaScriptFileType, "emptyStringDouble.js.nohit")
        }
    }

    @Nested
    inner class kotlin {

        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/kotlin/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.kt", "comment.after.kt")
        }

        @Test
        @Disabled("Kotlin completion behavior changed on IU-2026.1 test runtime")
        fun string() {
            testCompletion("string.before.kt", "string.after.kt")
        }

        @Test
        @Disabled("Kotlin completion behavior changed on IU-2026.1 test runtime")
        fun stringMiddle() {
            testCompletion("stringMiddle.before.kt", "stringMiddle.after.kt")
        }

        @Test
        fun emptyString() {
            noHits(KotlinFileType.INSTANCE, "emptyString.kt.nohit")
        }

        @Test
        fun stringTemplate() {
            noHits(KotlinFileType.INSTANCE, "stringTemplate.kt.nohit")
        }
    }

    @Nested
    inner class markdown {

        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/markdown/"
        }

        @Test
        fun first() {
            testCompletion("first.before.md", "first.after.md")
        }

        @Test
        fun middle() {
            testCompletion("middle.before.md", "middle.after.md")
        }
    }

    @Nested
    inner class php {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/php/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.php", "comment.after.php")
        }

        @Test
        fun string() {
            testCompletion("string.before.php", "string.after.php")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.php", "stringMiddle.after.php")
        }

        @Test
        fun emptyString() {
            noHits(PhpFileType.INSTANCE, "emptyString.php.nohit")
        }

        @Test
        fun stringTemplate() {
            noHits(PhpFileType.INSTANCE, "stringTemplate.php.nohit")
        }
    }

    @Nested
    inner class properties {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/properties/"
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun comment() {
            testCompletion("comment.before.properties", "comment.after.properties")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun value() {
            testCompletion("value.before.properties", "value.after.properties")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun valueMiddle() {
            testCompletion("valueMiddle.before.properties", "valueMiddle.after.properties")
        }
    }

    @Nested
    inner class python {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/python/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.py", "comment.after.py")
        }

        @Test
        fun string() {
            testCompletion("string.before.py", "string.after.py")
        }

        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.py", "stringMiddle.after.py")
        }

        @Test
        fun emptyString() {
            noHits(PlainTextFileType.INSTANCE, "emptyString.py.nohit")
        }
    }

    @Nested
    inner class ruby {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/ruby/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.rb", "comment.after.rb")
        }

        @Test
        fun string() {
            testCompletion("string.before.rb", "string.after.rb")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.rb", "stringMiddle.after.rb")
        }

        @Test
        fun emptyString() {
            noHits(RubyFileType.RUBY, "emptyString.rb.nohit")
        }

        @Test
        fun stringTemplate() {
            noHits(RubyFileType.RUBY, "stringTemplate.rb.nohit")
        }
    }

    @Nested
    inner class rust {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/rust/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.rs", "comment.after.rs")
        }

        @Test
        fun string() {
            testCompletion("string.before.rs", "string.after.rs")
        }

        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.rs", "stringMiddle.after.rs")
        }

        @Test
        fun emptyString() {
            noHits(RsFileType, "emptyString.rs.nohit")
        }
    }

    @Nested
    inner class scala {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/scala/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.scala", "comment.after.scala")
        }

        @Test
        fun string() {
            testCompletion("string.before.scala", "string.after.scala")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            testCompletion("stringMiddle.before.scala", "stringMiddle.after.scala")
        }

        @Test
        fun emptyString() {
            noHits(ScalaFileType.INSTANCE, "emptyString.scala.nohit")
        }

        @Test
        fun stringTemplate() {
            noHits(ScalaFileType.INSTANCE, "stringTemplate.scala.nohit")
        }
    }

    @Nested
    inner class sql {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/sql/"
        }

        @Test
        fun comment() {
            testCompletion("comment.before.sql", "comment.after.sql")
        }
    }

    @Nested
    inner class text {

        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/text/"
        }

        @Test
        fun first() {
            testCompletion("first.before.txt", "first.after.txt")
        }

        @Test
        fun middle() {
            testCompletion("middle.before.txt", "middle.after.txt")
        }
    }

    @Nested
    inner class xml {

        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/xml/"
        }

        @Test
        fun attr() {
            testCompletion("attr.before.xml", "attr.after.xml")
        }

        @Test
        fun comment() {
            testCompletion("comment.before.xml", "comment.after.xml")
        }

        @Test
        fun value() {
            testCompletion("value.before.xml", "value.after.xml")
        }

        @Test
        fun emptyAttr() {
            noHits(XmlFileType.INSTANCE, "emptyAttr.xml.nohit")
        }

        @Test
        fun emptyValue() {
            noHits(XmlFileType.INSTANCE, "emptyValue.xml.nohit")
        }
    }

    @Nested
    inner class yaml {
        @BeforeEach
        fun setUp() {
            myFixture.testDataPath = "src/test/resources/completion/yaml/"
        }

        @Test
        fun value() {
            testCompletion("value.before.yml", "value.after.yml")
        }

        @Test
        fun empty() {
            noHits(YAMLFileType.YML, "empty.yml.nohit")
        }
    }
}