package com.sergstas.lib.sql

import com.sergstas.extensions.select
import kotlin.reflect.KClass

public class TableInfo {
    public val name: String
    public val columns: ArrayList<ColumnInfo<Any>>
    public val columnsCount: Int get() {
        return columns.count()
    }

    public constructor(name: String) {
        this.name = name
        columns = ArrayList()
    }

    public fun getColumnsNames(): Array<String> {
        var result = emptyArray<String>()
        for (e in columns.select { c -> c.name })
            result += e
        return result
    }

    public fun<T: Any> addColumn(name: String, type: KClass<T>) : Boolean {
        if (columns.select { e: ColumnInfo<Any> -> e.name  }.contains(name))
            return false
        columns.add(ColumnInfo(name, type as KClass<Any>))
        return true
    }




}