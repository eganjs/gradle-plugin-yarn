package io.eganjs.gradle.plugin.yarn

import org.junit.rules.TemporaryFolder
import java.io.File

fun TemporaryFolder.setupProject(): File =
        this.root.apply {
            resolve("settings.gradle").writeText("")
            resolve("build.gradle").writeText("""
                plugins {
                    id('io.eganjs.yarn')
                }
            """)
            resolve("package.json").writeText("""
                {
                }
            """)
        }