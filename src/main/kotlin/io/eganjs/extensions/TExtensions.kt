package io.eganjs.extensions

fun <T> T.filterT(predicate: (T) -> Boolean) =
        if (predicate(this)) this else null
