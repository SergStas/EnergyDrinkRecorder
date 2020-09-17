package com.sergstas.lib.extensions

import android.database.Cursor
import java.sql.Blob
import kotlin.reflect.KClass
import kotlin.reflect.cast

public fun Cursor.getIntByName(name: String): Int? {
    val index = this.getColumnIndex(name)
    return try {
        this.getInt(index)
    }
    catch (e: Exception) {
        null
    }
}

public fun Cursor.getStringByName(name: String): String? {
    val index = this.getColumnIndex(name)
    return try {
        this.getString(index)
    }
    catch (e: Exception) {
        null
    }
}

public fun Cursor.getFloatByName(name: String): Float? {
    val index = this.getColumnIndex(name)
    return try {
        this.getFloat(index)
    }
    catch (e: Exception) {
        null
    }
}

public fun Cursor.getDoubleByName(name: String): Double? {
    val index = this.getColumnIndex(name)
    return try {
        this.getDouble(index)
    }
    catch (e: Exception) {
        null
    }
}

public fun Cursor.getBlobByName(name: String): ByteArray? {
    val index = this.getColumnIndex(name)
    return try {
        this.getBlob(index)
    }
    catch (e: Exception) {
        null
    }
}

public fun Cursor.getShortByName(name: String): Short? {
    val index = this.getColumnIndex(name)
    return try {
        this.getShort(index)
    }
    catch (e: Exception) {
        null
    }
}

public fun Cursor.getLongByName(name: String): Long? {
    val index = this.getColumnIndex(name)
    return try {
        this.getLong(index)
    }
    catch (e: Exception) {
        null
    }
}

@ExperimentalStdlibApi
public fun Cursor.tryGetValue(name: String, type: KClass<Any>) : Any? {
    return when(type) {
        Int::class -> type.cast(this.getIntByName(name))
        String::class -> type.cast(this.getStringByName(name))
        Double::class -> type.cast(this.getDoubleByName(name))
        Float::class -> type.cast(this.getFloatByName(name))
        Short::class -> type.cast(this.getShortByName(name))
        Long::class -> type.cast(this.getLongByName(name))
        Blob::class -> type.cast(this.getBlobByName(name))
        else -> null
    }
}

public fun Cursor.valueToString(name: String): String? {
    return when {
        this.getStringByName(name) != null -> this.getStringByName(name)
        this.getDoubleByName(name) != null -> this.getDoubleByName(name).toString()
        this.getIntByName(name) != null -> this.getIntByName(name).toString()
        this.getFloatByName(name) != null -> this.getFloatByName(name).toString()
        this.getShortByName(name) != null -> this.getShortByName(name).toString()
        this.getLongByName(name) != null -> this.getLongByName(name).toString()
        this.getBlobByName(name) != null -> this.getBlobByName(name).toString()
        else -> null
    }
}

public fun Cursor.valuesToStrTemplate(template: String, names: Iterable<String>): String? {
    val stringValues = ArrayList<String?>()
    for (name in names)
        stringValues.add(this.valueToString(name))
    return stringValues.format(template)
}