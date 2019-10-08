package io.eganjs.gradle.plugin.yarn

import com.jayway.jsonpath.JsonPath
import java.io.File
import java.io.Reader

object Json {
    fun <T> read(json: Reader, jsonPath: String, clazz: Class<T>): T =
            JsonPath.parse(json).read(jsonPath, clazz)

    inline fun <reified T> read(json: Reader, jsonPath: String): T =
            read(json, jsonPath, T::class.java)

    inline fun <reified T> File.readJsonAt(jsonPath: String) =
            read<T>(this.reader(), jsonPath)
}
