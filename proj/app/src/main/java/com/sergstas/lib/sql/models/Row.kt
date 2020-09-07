package com.sergstas.lib.sql.models

import android.database.Cursor
import com.sergstas.extensions.*

public class Row {
    public val parent: TableInfo
    public val columns: ArrayList<ColumnInfo<Any>>
        get() {return parent.columns}

    public var values: ArrayList<Any?>
    public var isFilled: Boolean = false
        private set

    public val valuesParamsString
        get() = if (!isFilled) null else values.select { v -> v.toString() }.format(getValuesStrPlaceholders().joinToString(", "))

    public constructor(table: TableInfo) {
        parent = table
        values = ArrayList()
    }

    public fun fill(params: Iterable<Any?>): Boolean {
        values.clear()
        if (params.count() != columns.count()) {
            isFilled = false
            return false
        }
        for ((i, e) in params.withIndex()) {
            //var q1 = columns[i].type
            //var q2 = if (e != null) e::class else columns[i].type
            if (e != null && columns[i].type != e::class) {
                isFilled = false
                return false
            }
            values.add(e)
        }
        isFilled = true
        return true
    }

    public fun getValue(columnName: String): Any? {
        val index = columns.indexOf(columns.firstOrNull{ c-> c.name == columnName})
        if (index == -1)
            return null
        return values[index]
    }

    @ExperimentalStdlibApi
    public fun fillFromCursor(cursor: Cursor): Boolean {
        var args = emptyArray<Any?>()
        for (column in columns) {
            if (cursor.tryGetValue(column.name, column.type) == null)
                return false
            else
                args += cursor.tryGetValue(column.name, column.type)
        }
        return fill(args.toArrayList())
    }

    private fun getValuesStrPlaceholders(): Iterable<String> {
        val result = ArrayList<String>()
        for (info in columns)
            result.add(if(info.type == String::class) "'%s'" else "%s")
        return result
    }
}