plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellij {
    version.set("2025.1")
    type.set("IU") // Target IDE Platform

    plugins.set(listOf(
        "Pythonid:251.23774.460", // https://plugins.jetbrains.com/plugin/631-python
        "org.jetbrains.plugins.ruby:251.23774.435", // https://plugins.jetbrains.com/plugin/1293-ruby
        "yaml",
        "org.jetbrains.plugins.go:251.23774.435", // https://plugins.jetbrains.com/plugin/9568-go
//        "IntelliLang",
        "com.jetbrains.php:251.23774.466", // https://plugins.jetbrains.com/plugin/6610-php
//        "JavaScriptLanguage",
        "JavaScript",
        "markdown",
        "Groovy",
        "org.intellij.scala:2025.1.20", // https://plugins.jetbrains.com/plugin/1347-scala
        "com.jetbrains.rust:251.23774.463", // https://plugins.jetbrains.com/plugin/22407-rust
        "com.intellij.css",
        "java-i18n",
        "properties",
        "xml-refactoring",
//        "coverage"
    ))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("253.*")
        changeNotes.set(project.file("LATEST.txt").readText())
    }

    publishPlugin {
        token.set(System.getenv("HUB_TOKEN"))
        channels.set(listOf(System.getProperty("CHANNELS") ?: "beta"))
    }

    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}
