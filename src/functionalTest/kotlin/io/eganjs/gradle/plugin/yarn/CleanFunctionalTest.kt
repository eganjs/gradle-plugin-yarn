package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

class CleanFunctionalTest {
    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `when clean is run then the clean script from the package json is run`() {
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
            withArguments("clean")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).contains("$ echo running clean")
    }
}
