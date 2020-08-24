package com.sergstas.lib.sql

public class Row {
    public val parent: TableInfo
    public val columns: ArrayList<ColumnInfo<Any>>
        get() {return parent.columns}

    public var values: ArrayList<Any?>
    public var isFilled: Boolean = false
        private set

    public constructor(table: TableInfo) {
        parent = table
        values = ArrayList()
    }

    public fun fill(params: Iterable<Any?>): Boolean {
        if (params.count() != columns.count()) {
            isFilled = false
            return false
        }
        for ((i, e) in params.withIndex()) {
            var q1 = columns[i].type
            var q2 = if (e != null) e::class else columns[i].type
            if (e != null && columns[i].type != e::class) {
                isFilled = false
                return false
            }
            values.add(e)
        }
        isFilled = true
        return true
    }
}