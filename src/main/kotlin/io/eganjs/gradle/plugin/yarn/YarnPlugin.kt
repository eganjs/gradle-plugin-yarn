package io.eganjs.gradle.plugin.yarn

import io.eganjs.extensions.filterT
import io.eganjs.extensions.toPascalCase
import io.eganjs.gradle.plugin.yarn.Json.readJsonAt
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskContainer
import java.io.File

class YarnPlugin : Plugin<Project> {

    companion object {
        private const val EXTENSION = "yarn"

        object Tasks {
            val install = YarnTaskDescription("install", listOf("install"))
        }

        private val baseTasks: Set<YarnTaskDescription> = setOf(Tasks.install)
    }

    override fun apply(project: Project) {
        project.plugins.apply(BasePlugin::class.java)

        val scripts = getScriptsFromPackageJson(project)

        val extension = project.extensions.create(EXTENSION, YarnExtension::class.java, project)

        val yarnBaseTasks = baseTasks.map { (name, args) ->
            project.createYarnTask(name, *(args.toTypedArray()))
        }

        val yarnRunTasks = scripts.map { (scriptAlias, _) ->
            val task = project.createYarnTask(createYarnTaskName(scriptAlias), "run", scriptAlias)
            task.dependsOn(yarnBaseTasks)
        }

        val yarnTasks = (yarnBaseTasks + yarnRunTasks)
                .map { it.name to it }
                .toMap()

        extension.scriptHooks.forEach { (taskName, dependencyNames) ->
            val dependencies = dependencyNames
                    .map {
                        val scriptTaskName = createYarnTaskName(it)

                        if (scriptTaskName !in yarnTasks) {
                            throw GradleException("Could not finf")
                        }

                        yarnTasks[scriptTaskName]
                    }

            project.tasks.getByName(taskName).dependsOn(dependencies)
        }
    }

    private fun Task.requiredBy(task: String) {
        project.tasks.findByName(task)?.dependsOn(this)
    }

    private fun Project.createYarnTask(taskName: String, vararg yarnArgs: String): YarnTask {
        val taskCommand = listOf("yarn", yarnArgs).joinToString(separator = " ")

        return project.tasks.create(taskName, YarnTask::class.java).apply {
            group = "yarn"
            description = "Runs \"$taskCommand\""
            args(yarnArgs)
        }
    }

    private fun createYarnTaskName(name: String) = "yarn" + name.toPascalCase()

    private inline fun <reified T : Task> TaskContainer.create(name: String, configure: T.() -> Unit): T =
            this.create(name, T::class.java).apply(configure)

    private fun getScriptsFromPackageJson(project: Project): Map<String, String> =
            project
                    .projectDir
                    .packageJson
                    .filterT { it.exists() }
                    ?.readJsonAt("$.scripts")
                    ?: emptyMap()

    private val File.packageJson: File
        get() = File(this, "package.json")
}



