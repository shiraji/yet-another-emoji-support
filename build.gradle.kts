plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.20" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    id("org.jetbrains.intellij.platform") version "2.13.1" // https://plugins.gradle.org/plugin/org.jetbrains.intellij.platform
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") // python-psi
    intellijPlatform {
        defaultRepositories()
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "com.github.shiraji.yaemoji"
        name = "Yet another emoji support"
        version = project.version.toString()
        changeNotes.set(project.file("LATEST.txt").readText())
    }
    publishing {
        token.set(System.getenv("HUB_TOKEN"))
        channels.set(listOf(System.getProperty("CHANNELS") ?: "beta"))
    }
    caching {
        ides {
            enabled = true
        }
    }
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
        // untilBuild.set("251.*") Remove for "Open-End" Compatibility. @see https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html#open-end-compatibility
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
    intellijPlatform { intellijIdeaUltimate("2026.1")

        bundledPlugin("JavaScript")
        bundledPlugin("com.intellij.css")
        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.modules.xml")
        bundledPlugin("org.intellij.groovy")
        bundledPlugin("org.intellij.plugins.markdown")
        bundledPlugin("org.jetbrains.plugins.yaml")

        plugin("com.jetbrains.php:253.32098.37") // https://plugins.jetbrains.com/plugin/6610-php
        plugin("com.jetbrains.rust:253.31033.204") // https://plugins.jetbrains.com/plugin/22407-rust
        plugin("org.intellij.scala:2025.3.39") // https://plugins.jetbrains.com/plugin/1347-scala
        plugin("org.jetbrains.plugins.go:253.32098.37") // https://plugins.jetbrains.com/plugin/9568-go
        plugin("org.jetbrains.plugins.ruby:253.32098.37") // https://plugins.jetbrains.com/plugin/1293-ruby
        plugin("org.jetbrains.kotlin:253.32098.37-IJ") // https://plugins.jetbrains.com/plugin/6954-kotlin

        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.JUnit5)
    }

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.20") // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib

    // python-psi-api provides PyStringLiteralExpression and other Python PSI interfaces.
    // Highest available version matching 2025.3 (253.x) builds.
    compileOnly("com.jetbrains.intellij.python:python-psi:253.32098.37")

    testImplementation("io.mockk:mockk:1.14.9") // https://mvnrepository.com/artifact/io.mockk/mockk
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.3") // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.14.3") // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.assertj:assertj-core:3.27.7") // https://mvnrepository.com/artifact/org.assertj/assertj-core
}
