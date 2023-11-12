plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2")
    type.set("IU") // Target IDE Platform

    plugins.set(listOf(
        "Kotlin",
        "Pythonid:232.8660.185", // https://plugins.jetbrains.com/plugin/631-python
        "org.jetbrains.plugins.ruby:232.8660.185", // https://plugins.jetbrains.com/plugin/1293-ruby
        "yaml",
        "org.jetbrains.plugins.go:232.8660.142", // https://plugins.jetbrains.com/plugin/9568-go
//        "IntelliLang",
        "com.jetbrains.php:232.8660.205", // https://plugins.jetbrains.com/plugin/6610-php
//        "JavaScriptLanguage",
        "JavaScript",
        "markdown",
        "Groovy",
        "org.intellij.scala:2023.2.23", // https://plugins.jetbrains.com/plugin/1347-scala
        "org.rust.lang:0.4.201.5424-232", // https://plugins.jetbrains.com/plugin/8182-rust
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
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("232.*")
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
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
