package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test

/**
 * A simple functional test for the 'io.eganjs.yarn' plugin.
 */
class YarnPluginFunctionalTest {
    @Test fun `given a package json exists with a populated script object then corosponding tasks are generated`() {
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText("""
            plugins {
                id('io.eganjs.yarn')
            }
        """)
        projectDir.resolve("package.json").writeText("""
            {
              "scripts": {
                "build": "echo \"do the build\"",
                "serve": "echo \"run the code\""
              }
            }
        """)

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("-q", "tasks", "--group", "yarn")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        // Verify the result
        val outputLines = result.output.lines()
        assertThat(outputLines).doesNotContain("No tasks")
        assertThat(outputLines).containsOnlyOnce("yarn")
        assertThat(outputLines).containsOnlyOnce("yarnBuild")
        assertThat(outputLines).containsOnlyOnce("yarnServe")
    }
}
