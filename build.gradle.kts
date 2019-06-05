import org.jetbrains.intellij.tasks.PatchPluginXmlTask

buildscript {
    repositories {
        jcenter()
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("jacoco")
    id("org.jetbrains.intellij") version "0.4.8"
}

group = "com.github.shiraji"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "IU-2018.1.4"

    setPlugins(
            // https://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/plugin_compatibility.html
            "Kotlin",
            "Pythonid:2018.1.181.5087.50", // https://plugins.jetbrains.com/plugin/631-python
            "org.jetbrains.plugins.ruby:2018.1.20180515", // https://plugins.jetbrains.com/plugin/1293-ruby
            "yaml",
            "org.jetbrains.plugins.go:181.5087.39.204", // https://plugins.jetbrains.com/plugin/9568-go
            "com.jetbrains.php:181.5087.11", // https://plugins.jetbrains.com/plugin/6610-php
            "JavaScriptLanguage",
            "markdown"
    )
    updateSinceUntilBuild = false
}


val patchPluginXml: PatchPluginXmlTask by tasks

patchPluginXml {
    changeNotes(
        """
        <p>1.0.0</p>
        <ul>
          <li>Initial release</li>
        </ul>
        <p>Older version changes are listed on <a href="https://github.com/shiraji/yet-another-emoji-support/blob/master/CHANGELOG.md">CHANGELOG.md</a></p>
        """.trimIndent()
    )
}

dependencies {
    val kotlinVersion: String by project
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("io.mockk:mockk:1.8.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
}

configurations {
    create("ktlint")

    dependencies {
        add("ktlint", "com.github.shyiko:ktlint:0.30.0")
    }
}

tasks.register("ktlintCheck", JavaExec::class) {
    description = "Check Kotlin code style."
    classpath = configurations["ktlint"]
    main = "com.github.shyiko.ktlint.Main"
    args("src/**/*.kt")
}

tasks.register("ktlintFormat", JavaExec::class) {
    description = "Fix Kotlin code style deviations."
    classpath = configurations["ktlint"]
    main = "com.github.shyiko.ktlint.Main"
    args("-F", "src/**/*.kt")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
