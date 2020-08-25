package com.sergstas.lib.sql.dbcontrol

import android.content.Context
import com.sergstas.lib.sql.models.Row
import com.sergstas.lib.sql.models.TableInfo
import com.sergstas.lib.sql.res.StrConsts
import java.lang.Exception

class DBController public constructor(context: Context) {
    private val _context: Context = context
    private val _tables = HashMap<String, TableInfo>()
    private val _helpers = HashMap<String, OpenHelper>()

    public fun tryAddTable(table: TableInfo, id: String): Boolean {
        if (!table.initFinished)
            return false
        _tables[id] = table
        _helpers[id] = OpenHelper(_context, table)
        return true
    }

    public fun tryAddPosition(row: Row, tableId: String): Boolean {
        if (!_tables.containsKey(tableId) || !row.isFilled)
            return false
        val table = _tables[tableId]
        val db = _helpers[tableId]!!.writableDatabase
        val query = String.format(StrConsts.QUERY_ADD, table!!.name, table!!.columnsParamsString, row.valuesParamsString)
        return try {
            db.execSQL(query)
            true
        } catch (e: Exception) {
            false
        }
    }
}