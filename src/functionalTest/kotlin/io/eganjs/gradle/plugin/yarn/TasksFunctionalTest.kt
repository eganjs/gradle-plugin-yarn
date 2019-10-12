package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

class TasksFunctionalTest {
    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `when tasks are listed then expected tasks exist`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject()

        // Run the build
        val result = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("-q", "tasks", "--group", "yarn")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        val outputLines = result.output.lines().filter { it.startsWith("yarn") }
        assertThat(outputLines).containsExactlyInAnyOrder(
                "yarnInstall - Installs all dependencies for a project",
                "yarnCInstall - Installs all dependencies for a project (specifically for yarnRunClean)",
                "yarnRunBuildProd - Runs the `build:prod` package script",
                "yarnRunClean - Runs the `clean` package script",
                "yarnRunLint - Runs the `lint` package script",
                "yarnRunTest - Runs the `test` package script"
        )
    }
}
