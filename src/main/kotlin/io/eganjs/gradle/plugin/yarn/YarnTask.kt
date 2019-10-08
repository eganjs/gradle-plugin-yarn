package io.eganjs.gradle.plugin.yarn

import org.gradle.api.tasks.Exec

open class YarnTask : Exec() {
    init {
        executable = "yarn"
    }
}
