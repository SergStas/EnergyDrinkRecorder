package com.sergstas.lib.sql.dbcontrol

import android.content.Context
import android.database.Cursor
import com.sergstas.extensions.format
import com.sergstas.lib.sql.models.Row
import com.sergstas.lib.sql.models.TableInfo
import com.sergstas.lib.sql.res.StrConstants
import java.lang.Exception
import java.util.zip.DataFormatException
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

    public fun getTable(tableId: String): TableInfo? {
        return if (_tables.containsKey(tableId)) _tables[tableId] else null
    }

    public fun tryAddPosition(tableId: String, row: Row): Boolean {
        if (!_tables.containsKey(tableId) || !row.isFilled)
            return false
        val table = _tables[tableId]
        val db = _helpers[tableId]!!.writableDatabase
        val query = String.format(StrConstants.QUERY_ADD, table!!.name, table!!.columnsParamsString, row.values.format(row.valuesParamsString!!))
        return try {
            db.rawQuery(query, null)
            true
        } catch (e: Exception) {
            false
        }
    }

    @ExperimentalStdlibApi
    public fun tryRemoveBy(tableId: String, columnName: String, value: Any?): Boolean {
        if (!_tables.containsKey(tableId) || !_tables[tableId]!!.containsColumn(columnName))
            return false
        val table = _tables[tableId]!!
        val column = table.getColumn(columnName)!!
        val castedId = (column.type).cast(value)
        val db = _helpers[tableId]!!.writableDatabase//readableDatabase
        val query = String.format(StrConstants.QUERY_DELETE_FROM_WHERE, table.name, columnName,
            if (column.type == String::class) "`$castedId`" else castedId.toString())
        return try {
            db.rawQuery(query, null)
            true
        }
        catch (e: Exception) {
            false
        }
    }

    public fun tryClear(tableId: String): Boolean {
        if (!_tables.containsKey(tableId))
            return false
        return try {
            _helpers[tableId]!!.writableDatabase.rawQuery(String.format(StrConstants.QUERY_DELETE_ALL, _tables[tableId]!!.name), null)
            true
        }
        catch (e: Exception) {
            false
        }
    }

    @ExperimentalStdlibApi
    public fun selectBy(tableId: String, columnName: String, value: Any?): Iterable<Row>? {
        if (!_tables.containsKey(tableId) || !_tables[tableId]!!.containsColumn(columnName))
            return null
        val table = _tables[tableId]!!
        val castedId = (table.getColumn(columnName)!!.type).cast(value)
        val db = _helpers[tableId]!!.readableDatabase
        val query = String.format(StrConstants.QUERY_SELECT_WHERE, table.name, columnName, castedId)
        val cursor: Cursor
        try {
            cursor = db.rawQuery(query, null)
        }
        catch (e: Exception) {
            return null
        }
        val result = ArrayList<Row>()
        while (cursor.moveToNext()) {
            val row = Row(table)
            if (!row.fillFromCursor(cursor))
                return null
            result.add(row)
        }
        return result
    }

    @ExperimentalStdlibApi
    public fun getAllPositions(tableId: String): ArrayList<Row> {
        if (!_tables.containsKey(tableId))
            throw Exception("Table key $tableId was not found in DBController")
        val result = ArrayList<Row>()
        val table = _tables[tableId]!!
        val cursor = _helpers[tableId]!!.readableDatabase.rawQuery(String.format(StrConstants.QUERY_SELECT_ALL, table.name), null)
        while (cursor.moveToNext()) {
            val row = Row(table)
            if (!row.fillFromCursor(cursor))
                throw DataFormatException("Failed to fill row from cursor")
            result.add(row)
        }
        return result
    }
}