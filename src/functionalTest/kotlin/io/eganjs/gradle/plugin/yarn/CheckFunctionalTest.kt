package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

class CheckFunctionalTest {
    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `when check is run then the lint and test script from the package json is run`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject().apply {
            resolve("package.json").writeText("""
                {
                  "scripts": {
                    "lint": "echo running lint",
                    "test": "echo running test"
                  }
                }
            """)
        }

        // Run the build
        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("check")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ echo running lint")
        assertThat(outputLines).contains("$ echo running test")
    }
}
