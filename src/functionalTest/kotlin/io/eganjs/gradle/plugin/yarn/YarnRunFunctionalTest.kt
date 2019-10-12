package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

class YarnRunFunctionalTest {
    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `when yarnRunClean is run then the clean script from the package json is run`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject().apply {
            resolve("package.json").writeText("""
                {
                  "scripts": {
                    "clean": "echo running clean"
                  }
                }
            """)
        }

        // Run the build
        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnRunClean")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ echo running clean")
    }

    @Test
    fun `when yarnRunBuildProd is run then the build script from the package json is run`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject().apply {
            resolve("package.json").writeText("""
                {
                  "scripts": {
                    "build:prod": "echo running build:prod"
                  }
                }
            """)
        }

        // Run the build
        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnRunBuildProd")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ echo running build:prod")
    }

    @Test
    fun `when yarnRunLint is run then the lint script from the package json is run`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject().apply {
            resolve("package.json").writeText("""
                {
                  "scripts": {
                    "lint": "echo running lint"
                  }
                }
            """)
        }

        // Run the build
        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnRunLint")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ echo running lint")
    }

    @Test
    fun `when yarnRunTest is run then the test script from the package json is run`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject().apply {
            resolve("package.json").writeText("""
                {
                  "scripts": {
                    "test": "echo running test"
                  }
                }
            """)
        }

        // Run the build
        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnRunTest")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ echo running test")
    }

    @Test
    fun `when yarnRunClean yarnRunBuildProd is run after yarnInstall then the clean and build scripts from the package json are run`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject().apply {
            resolve("package.json").writeText("""
                {
                  "scripts": {
                    "clean": "rimraf node_modules dist",
                    "build:prod": "mkdirp dist"
                  },
                  "devDependencies": {
                    "rimraf": "3.0.0",
                    "mkdirp": "0.5.1"
                  }
                }
            """)
        }

        // Run the build
        GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnInstall")
            withProjectDir(projectDir)
        }.build()

        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnRunClean", "yarnRunBuildProd")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ rimraf node_modules dist")
        assertThat(outputLines).contains("$ mkdirp dist")
    }
}
