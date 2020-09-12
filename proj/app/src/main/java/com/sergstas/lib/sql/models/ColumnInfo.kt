package com.sergstas.lib.sql.models

import kotlin.reflect.KClass

public class ColumnInfo<T: Any> public constructor(public val name: String, public val type: KClass<T>) {
    public var isIndex = false
    private set

    public constructor(name: String, type: KClass<T>, isIndex: Boolean) : this(name, type) {
        this.isIndex = isIndex
    }
}