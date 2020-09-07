package com.sergstas.lib.sql.models

import com.sergstas.extensions.joinToString
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import com.sergstas.extensions.where
import kotlin.reflect.KClass

public class TableInfo {
    public val name: String
    public val columns: ArrayList<ColumnInfo<Any>>
    public val columnsCount: Int get() {
        return columns.count()
    }

    public val indexers: ArrayList<ColumnInfo<Any>> get() {
        return columns.where{ c -> c.isIndex}.toArrayList()
    }

    public val columnsParamsString
        get() = if (!initFinished) null else columns.select { c -> c.name }.joinToString(", ")

    public var initFinished = false
        private set

    public constructor(name: String) {
        this.name = name
        columns = ArrayList()
    }

    public fun finishInit() {
        //call after adding columns
        initFinished = true
    }

    public fun getColumn(name: String): ColumnInfo<Any>? {
        return columns.firstOrNull { c -> c.name == name }
    }

    public fun containsColumn(name: String): Boolean {
        return columns.select { c -> c.name }.contains(name)
    }

    public fun getColumnsNames(): Array<String>? {
        if (!initFinished)
            return null
        var result = emptyArray<String>()
        for (e in columns.select { c -> c.name })
            result += e
        return result
    }

    public fun<T: Any> addColumn(name: String, type: KClass<T>) : Boolean {
        if (initFinished || columns.select { e: ColumnInfo<Any> -> e.name  }.contains(name))
            return false
        columns.add(
            ColumnInfo(
                name,
                type as KClass<Any>
            )
        )
        return true
    }

    public fun<T: Any> addColumn(name: String, type: KClass<T>, isIndex: Boolean) : Boolean {
        if (initFinished || columns.select { e: ColumnInfo<Any> -> e.name  }.contains(name) ||
                columns.select { c -> c.isIndex }.contains(true) && isIndex) //idk why
            return false
        columns.add(
            ColumnInfo(
                name,
                type as KClass<Any>,
                isIndex
            )
        )
        return true
    }




}