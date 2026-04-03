plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.20" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    id("org.jetbrains.intellij.platform") version "2.13.1" // https://plugins.gradle.org/plugin/org.jetbrains.intellij.platform
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"
val ideaUltimateVersion = "2026.1"

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
    pluginVerification {
        ides {
            val verifierIdeVersion = System.getProperty("VERIFIER_IDE_VERSION") ?: ideaUltimateVersion
            create(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaUltimate, verifierIdeVersion)
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
        sinceBuild.set("252")
        // untilBuild.set("251.*") Remove for "Open-End" Compatibility. @see https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html#open-end-compatibility
    }

    publishPlugin {
        token.set(System.getenv("HUB_TOKEN"))
        channels.set(listOf(System.getProperty("CHANNELS") ?: "beta"))
    }

    test {
        useJUnitPlatform {
            excludeEngines("junit-vintage")
        }
        systemProperty("junit.vintage.engine.enabled", "false")
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaUltimate(ideaUltimateVersion)

        bundledPlugin("JavaScript")
        bundledPlugin("com.intellij.css")
        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.modules.xml")
        bundledPlugin("org.intellij.groovy")
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("org.intellij.plugins.markdown")
        bundledPlugin("org.jetbrains.plugins.yaml")

        plugin("com.jetbrains.php:253.32098.37") // https://plugins.jetbrains.com/plugin/6610-php
        plugin("com.jetbrains.rust:253.31033.204") // https://plugins.jetbrains.com/plugin/22407-rust
        plugin("org.intellij.scala:2025.3.39") // https://plugins.jetbrains.com/plugin/1347-scala
        plugin("org.jetbrains.plugins.go:253.32098.37") // https://plugins.jetbrains.com/plugin/9568-go
        plugin("org.jetbrains.plugins.ruby:253.32098.37") // https://plugins.jetbrains.com/plugin/1293-ruby

        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.20") // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib

    // python-psi-api provides PyStringLiteralExpression and other Python PSI interfaces.
    // Highest available version matching 2025.3 (253.x) builds.
    compileOnly("com.jetbrains.intellij.python:python-psi:253.32098.37")

    testImplementation("io.mockk:mockk:1.14.9") { // https://mvnrepository.com/artifact/io.mockk/mockk
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-bom")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
    }
    testImplementation(platform("org.junit:junit-bom:5.13.4")) // align with the JUnit Platform launcher used by the IntelliJ test runtime
    testCompileOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    // IntelliJ test fixture internals still reference these JUnit4 classes at runtime.
    testRuntimeOnly("junit:junit:4.13.2")
}
