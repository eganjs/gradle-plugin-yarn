package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

class YarnInstallFunctionalTest {
    @Rule
    @JvmField
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    @Test
    fun `when yarnInstall is run then a yarn lock file and node_modules directory is created`() {
        // Setup the test build
        val projectDir = testProjectDir.setupProject()

        // Run the build
        GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments("yarnInstall")
            withProjectDir(projectDir)
        }.build()

        // Verify the result
        assertThat(projectDir.resolve("yarn.lock")).isFile
        assertThat(projectDir.resolve("node_modules")).isDirectory
    }
}
