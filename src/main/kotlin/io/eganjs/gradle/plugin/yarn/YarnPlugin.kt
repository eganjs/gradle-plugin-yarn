package io.eganjs.gradle.plugin.yarn

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.* // ktlint-disable no-wildcard-imports

class YarnPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        plugins.apply(BasePlugin::class.java)

        tasks {
            // Register install task

            val yarnInstall by registering(Exec::class) {
                description = "Installs all dependencies for a project"
                group = "yarn"
                commandLine("yarn", "install")
            }

            // Register separate install task for clean to prevent (https://github.com/eganjs/gradle-plugin-yarn/issues/3)

            val yarnCInstall by registering(Exec::class) {
                description = "Installs all dependencies for a project (specifically for yarnRunClean)"
                group = "yarn"
                commandLine("yarn", "install")
            }

            // Register package script tasks

            val yarnRunClean by registering(Exec::class) {
                description = "Runs the `clean` package script"
                group = "yarn"
                commandLine("yarn", "run", "clean")
                dependsOn(yarnCInstall)
            }

            val yarnRunBuildProd by registering(Exec::class) {
                description = "Runs the `build:prod` package script"
                group = "yarn"
                commandLine("yarn", "run", "build:prod")
                dependsOn(yarnInstall)
            }

            val yarnRunLint by registering(Exec::class) {
                description = "Runs the `lint` package script"
                group = "yarn"
                commandLine("yarn", "run", "lint")
                dependsOn(yarnInstall)
            }

            val yarnRunTest by registering(Exec::class) {
                description = "Runs the `test` package script"
                group = "yarn"
                commandLine("yarn", "run", "test")
                dependsOn(yarnInstall)
            }

            // Add lifecycle hooks

            @Suppress("UNUSED_VARIABLE")
            val clean by existing {
                dependsOn(yarnRunClean)
            }

            @Suppress("UNUSED_VARIABLE")
            val assemble by existing {
                dependsOn(yarnRunBuildProd)
            }

            @Suppress("UNUSED_VARIABLE")
            val check by existing {
                dependsOn(yarnRunLint, yarnRunTest)
            }
        }
    }
}
