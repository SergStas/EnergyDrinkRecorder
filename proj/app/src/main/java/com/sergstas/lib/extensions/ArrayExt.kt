package com.sergstas.lib.extensions

public fun<T> Array<T>.toArrayList() : ArrayList<T> {
    val result = ArrayList<T>()
    for (e in this)
        result.add(e)
    return result
}