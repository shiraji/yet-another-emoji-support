plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
        releases()
        marketplace()
    }
}

intellijPlatform {
    buildSearchableOptions = false
    instrumentCode = true
    projectName = "yet-another-emoji-support"
    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
        channels.set(listOf(System.getProperty("CHANNELS") ?: "beta"))
    }
    pluginConfiguration {
        changeNotes.set(project.file("CHANGELOG.md").readText())
        ideaVersion {
            sinceBuild.set("242.1")
            untilBuild.set("243.*")
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
        kotlinOptions.jvmTarget = "17"
    }

    test {
        useJUnitPlatform()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.1")

        bundledPlugin("com.intellij.java")
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("org.jetbrains.plugins.yaml")
        bundledPlugin("org.intellij.groovy")
        bundledPlugin("org.intellij.plugins.markdown")

        plugin("Refactor-X", "242.20224.170") // https://plugins.jetbrains.com/plugin/13119-refactor-x
        plugin("JavaScript", "242.21829.3") // https://plugins.jetbrains.com/plugin/22069-javascript-and-typescript
        plugin("PythonCore", "242.21829.3") // https://plugins.jetbrains.com/plugin/631-python
        plugin("org.jetbrains.plugins.ruby", "242.21829.3") // https://plugins.jetbrains.com/plugin/1293-ruby
        plugin("org.jetbrains.plugins.go", "242.21829.3") // https://plugins.jetbrains.com/plugin/9568-go
        plugin("com.jetbrains.php", "242.21829.3") // https://plugins.jetbrains.com/plugin/6610-php
        plugin("org.intellij.scala", "2024.2.1") // https://plugins.jetbrains.com/plugin/1347-scala
        plugin("com.jetbrains.rust", "242.19890.39") // https://plugins.jetbrains.com/plugin/8182-rust
        plugin("com.intellij.css", "242.21829.3") // https://plugins.jetbrains.com/plugin/22068-css/versions
        plugin("com.intellij.properties", "242.20224.155") // https://plugins.jetbrains.com/plugin/11594-properties

        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}
