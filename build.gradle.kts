import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn

plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
    `kotlin-dsl`
    kotlin("jvm") version "1.3.41"
    id("com.diffplug.gradle.spotless") version "3.24.3"
}

group = "io.eganjs"
version = "1.0.0"

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.assertj:assertj-core:3.13.2")
}

gradlePlugin {
    @Suppress("UNUSED_VARIABLE")
    val yarn by plugins.creating {
        id = "io.eganjs.yarn"
        displayName = "Yarn plugin"
        description = "Minimum viable plugin for orchestrating yarn from gradle"
        implementationClass = "io.eganjs.gradle.plugin.yarn.YarnPlugin"
    }
}

pluginBundle {
    website =  "https://github.com/eganjs/gradle-plugin-yarn"
    vcsUrl = "https://github.com/eganjs/gradle-plugin-yarn"
    tags = listOf("yarn", "nodejs", "orchestration")
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations.getByName("functionalTestImplementation").extendsFrom(configurations.getByName("testImplementation"))

val functionalTest by tasks.creating(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

val check by tasks.getting(Task::class) {
    dependsOn(functionalTest)
}

spotless {
    kotlin { ktlint() }
    kotlinGradle { ktlint() }
    format("misc") {
        target("README.md", ".gitignore")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}
