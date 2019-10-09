package io.eganjs.gradle.plugin.yarn

import org.assertj.core.api.Assertions.assertThat
import org.gradle.kotlin.dsl.apply
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test

class YarnPluginTest {
    @Test
    fun `plugin registers tasks`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(YarnPlugin::class)

        // Verify the result
        assertThat(project.tasks.names).contains(
                "yarnInstall",
                "yarnRunClean",
                "yarnRunBuildProd",
                "yarnRunLint",
                "yarnRunTest"
        )
    }
}
