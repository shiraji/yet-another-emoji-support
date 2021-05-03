import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("jacoco")
    id("org.jetbrains.intellij") version "0.7.3"
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    maxHeapSize = "3g"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
    }
}

jacoco {
    toolVersion = "0.8.2"
}

val jacocoTestReport by tasks.existing(JacocoReport::class) {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

repositories {
    mavenCentral()
    jcenter()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "IU-2020.2"

    setPlugins(
            // https://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/plugin_compatibility.html
            "Kotlin",
            "Pythonid:202.6397.20", // https://plugins.jetbrains.com/plugin/631-python
            "org.jetbrains.plugins.ruby:202.6397.20", // https://plugins.jetbrains.com/plugin/1293-ruby
            "yaml",
            "org.jetbrains.plugins.go:202.6397.20", // https://plugins.jetbrains.com/plugin/9568-go
            "com.jetbrains.php:202.6397.20", // https://plugins.jetbrains.com/plugin/6610-php
            "JavaScriptLanguage",
            "markdown",
            "Groovy",
            "org.intellij.scala:2020.2.3", // https://plugins.jetbrains.com/plugin/1347-scala
            "org.rust.lang:0.2.124.3162-202", // https://plugins.jetbrains.com/plugin/8182-rust
            "CSS",
            "java-i18n",
            "java",
            "properties",
            "coverage"
    )
    updateSinceUntilBuild = false
}

val patchPluginXml: PatchPluginXmlTask by tasks
patchPluginXml {
    changeNotes(project.file("LATEST.txt").readText())
    sinceBuild("202")
    untilBuild("999")
}

val publishPlugin: PublishTask by tasks
publishPlugin {
    token(System.getenv("HUB_TOKEN"))
    channels(System.getProperty("CHANNELS") ?: "beta")
}

dependencies {
    val kotlinVersion: String by project
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
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

tasks.register("resolveDependencies") {
    doLast {
        project.rootProject.allprojects.forEach {subProject ->
            subProject.buildscript.configurations.forEach {configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
            subProject.configurations.forEach {configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
        }
    }
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
