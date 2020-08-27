package com.sergstas.lib.sql.dbcontrol

import android.content.Context
import android.database.Cursor
import com.sergstas.lib.sql.models.Row
import com.sergstas.lib.sql.models.TableInfo
import com.sergstas.lib.sql.res.StrConsts
import java.lang.Exception
import kotlin.reflect.cast

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

    @ExperimentalStdlibApi
    public fun tryGetBy(columnName: String, value: Any?, tableId: String): Row? {
        if (!_tables.containsKey(tableId) || !_tables[tableId]!!.containsColumn(columnName))
            return null
        val table = _tables[tableId]!!
        val castedId = (table.getColumn(columnName)!!.type).cast(value)
        val db = _helpers[tableId]!!.readableDatabase
        val query = String.format(StrConsts.QUERY_SELECT_BY_ID, table.name, columnName, castedId)
        val cursor: Cursor
        try {
            cursor = db.rawQuery(query, null)
        }
        catch (e: Exception) {
            return null
        }
        val result = Row(table)
        return if (result.fillFromCursor(cursor)) result else null
    }

    @ExperimentalStdlibApi
    public fun tryGetAllPositions(tableId: String): ArrayList<Row>? {
        if (!_tables.containsKey(tableId))
            return null
        val result = ArrayList<Row>()
        val table = _tables[tableId]!!
        val cursor = _helpers[tableId]!!.readableDatabase.rawQuery(String.format(StrConsts.QUERY_SELECT_ALL, table.name), null)
        while (cursor.moveToNext()) {
            val row = Row(table)
            if (!row.fillFromCursor(cursor))
                return null
            result.add(row)
        }
        return result
    }
}