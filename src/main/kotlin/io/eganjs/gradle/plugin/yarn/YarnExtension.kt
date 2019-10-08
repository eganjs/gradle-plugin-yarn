package io.eganjs.gradle.plugin.yarn

data class YarnExtension(
        var scriptHooks: MutableMap<String, List<String>> = mutableMapOf(
                "assemble" to listOf("build"),
                "check" to listOf("lint")
        )
)
