package com.sergstas.lib.extensions

import java.lang.Exception

public fun String.tryParseInt(): Pair<Boolean, Int> {
    try {
        var result = Integer.parseInt(this)
        return Pair(true, result)
    }
    catch (e: Exception) {
        return Pair(false, 0)
    }
}