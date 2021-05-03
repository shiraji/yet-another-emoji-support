package com.github.shiraji.yaemoji.contributor

import com.github.shiraji.yaemoji.domain.EmojiCompletion
import com.github.shiraji.yaemoji.domain.EmojiDataManager
import com.goide.GoFileType
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.python.PythonFileType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.plugins.ruby.ruby.lang.RubyFileType
import org.jetbrains.plugins.scala.ScalaFileType
import org.jetbrains.yaml.YAMLFileType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.rust.lang.RsFileType

class AllEmojiCompletionContributorTests : BasePlatformTestCase() {

    @BeforeEach
    fun beforeEach() {
        setUp()
        val line = "548\t0x1F996\tT-Rex\tT-Rex | Tyrannosaurus Rex"
        val completion = EmojiCompletion.fromCsv(line)
        EmojiDataManager.emojiList.add(completion)
    }

    @AfterEach
    fun afterEach() {
        EmojiDataManager.emojiList.clear()
        tearDown()
    }

    private fun noHits(fileType: FileType, filePath: String) {
        val testFile = getFile(filePath)
        assertThat(testFile).isNotNull
        val content = testFile!!.inputStream.bufferedReader().readText()
        myFixture.configureByText(fileType, content)
        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings
        assertThat(lookupStrings).doesNotContain(":T-Rex: \uD83E\uDD96 (:Tyrannosaurus Rex:)")
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
            myFixture.testCompletion("comment.before.css", "comment.after.css")
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
            myFixture.testCompletion("comment.before.go", "comment.after.go")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.go", "string.after.go")
        }

        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.go", "stringMiddle.after.go")
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
            myFixture.testCompletion("comment.before.groovy", "comment.after.groovy")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.groovy", "string.after.groovy")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.groovy", "stringMiddle.after.groovy")
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
            myFixture.testCompletion("comment.before.java", "comment.after.java")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.java", "string.after.java")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.java", "stringMiddle.after.java")
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
            myFixture.testCompletion("comment.before.js", "comment.after.js")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.js", "string.after.js")
        }

        @Test
        fun stringBack() {
            myFixture.testCompletion("stringBack.before.js", "stringBack.after.js")
        }

        @Test
        fun stringDouble() {
            myFixture.testCompletion("stringDouble.before.js", "stringDouble.after.js")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.js", "stringMiddle.after.js")
        }

        @Test
        fun emptyString() {
            noHits(JavaScriptFileType.INSTANCE, "emptyString.js.nohit")
        }

        @Test
        fun emptyStringBack() {
            noHits(JavaScriptFileType.INSTANCE, "emptyStringBack.js.nohit")
        }

        @Test
        fun emptyStringDouble() {
            noHits(JavaScriptFileType.INSTANCE, "emptyStringDouble.js.nohit")
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
            myFixture.testCompletion("comment.before.kt", "comment.after.kt")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.kt", "string.after.kt")
        }

        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.kt", "stringMiddle.after.kt")
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
            myFixture.testCompletion("first.before.md", "first.after.md")
        }

        @Test
        fun middle() {
            myFixture.testCompletion("middle.before.md", "middle.after.md")
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
            myFixture.testCompletion("comment.before.php", "comment.after.php")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.php", "string.after.php")
        }

        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.php", "stringMiddle.after.php")
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

        @Test
        fun comment() {
            myFixture.testCompletion("comment.before.properties", "comment.after.properties")
        }

        @Test
        fun value() {
            myFixture.testCompletion("value.before.properties", "value.after.properties")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun valueMiddle() {
            myFixture.testCompletion("valueMiddle.before.properties", "valueMiddle.after.properties")
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
            myFixture.testCompletion("comment.before.py", "comment.after.py")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.py", "string.after.py")
        }

        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.py", "stringMiddle.after.py")
        }

        @Test
        fun emptyString() {
            noHits(PythonFileType.INSTANCE, "emptyString.py.nohit")
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
            myFixture.testCompletion("comment.before.rb", "comment.after.rb")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.rb", "string.after.rb")
        }

        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.rb", "stringMiddle.after.rb")
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
            myFixture.testCompletion("comment.before.rs", "comment.after.rs")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.rs", "string.after.rs")
        }

        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.rs", "stringMiddle.after.rs")
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
            myFixture.testCompletion("comment.before.scala", "comment.after.scala")
        }

        @Test
        fun string() {
            myFixture.testCompletion("string.before.scala", "string.after.scala")
        }

        @Disabled("Not sure why this does not work")
        @Test
        fun stringMiddle() {
            myFixture.testCompletion("stringMiddle.before.scala", "stringMiddle.after.scala")
        }

        @Test
        fun emptyString() {
            noHits(ScalaFileType.INSTANCE, "emptyString.scala.nohit")
        }

        @Disabled("NoMethodError happens")
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
            myFixture.testCompletion("comment.before.sql", "comment.after.sql")
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
            myFixture.testCompletion("first.before.txt", "first.after.txt")
        }

        @Test
        fun middle() {
            myFixture.testCompletion("middle.before.txt", "middle.after.txt")
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
            myFixture.testCompletion("attr.before.xml", "attr.after.xml")
        }

        @Test
        fun comment() {
            myFixture.testCompletion("comment.before.xml", "comment.after.xml")
        }

        @Test
        fun value() {
            myFixture.testCompletion("value.before.xml", "value.after.xml")
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
            myFixture.testCompletion("value.before.yml", "value.after.yml")
        }

        @Test
        fun empty() {
            noHits(YAMLFileType.YML, "empty.yml.nohit")
        }
    }
}
