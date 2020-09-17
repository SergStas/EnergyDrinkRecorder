package com.sergstas.lib.extensions

public fun <T, R> Iterable<T>.select(selector: (T) -> R): Iterable<R> {
    val result = ArrayList<R>()
    for (e in this)
        result.add(selector(e))
    return result
}

public fun<T> Iterable<T>.skip(index: Int) : Iterable<T> {
    val result = ArrayList<T>()
    for ((i, e) in this.withIndex()) {
        if (i < index)
            continue
        result.add(e)
    }
    return result
}

public fun<T> Iterable<T>.toArrayList(): ArrayList<T> {
    val result = ArrayList<T>()
    for (e in this)
        result.add(e)
    return result
}

public fun<T> Iterable<T>.format(template: String): String {
    // NOTICE: you shall use only "%s" placeholders
    val parts = template.split("%s")
    if (parts.count() == 0)
        return ""
    var i = 1
    var result = parts[0]
    for (e in this)
        if (i >= parts.count())
            return result
        else
            result += e.toString() + parts[i++]
    return result + parts.skip(i).stringConcat()
}

public fun<T> Iterable<T>.where(selector: (T) -> Boolean): Iterable<T> {
    val result = ArrayList<T>()
    for (e in this)
        if (selector(e))
            result.add(e)
    return result
}

public fun<T> Iterable<T>.stringConcat(): String {
    var result = ""
    for (e in this)
        result += e.toString()
    return result
}

public fun<T> Iterable<T>.joinToString(separator: String): String {
    var result = ""
    for ((i, e) in this.withIndex()) {
        result += e.toString()
        if (i != this.count() - 1)
            result += separator
    }
    return result
}