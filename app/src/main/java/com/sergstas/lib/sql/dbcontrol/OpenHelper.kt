package com.sergstas.lib.sql.dbcontrol

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sergstas.extensions.select
import com.sergstas.lib.sql.models.ColumnInfo
import com.sergstas.lib.sql.models.TableInfo
import com.sergstas.lib.sql.res.StrConstants
import java.lang.Exception

class OpenHelper(context: Context?, table: TableInfo) :
    SQLiteOpenHelper(context, table.name, null,
        VERSION
    ) {
    companion object {
        private const val VERSION = 1

        private fun<T: Any> columnToPartOfInitRequest(column: ColumnInfo<T>): String {
            return column.name +
                when (column.type) {
                    Int::class -> " integer" +
                        if (column.isIndex) " primary key autoincrement" else " not null default 0"
                    String::class -> " text not null"
                    Float::class -> " float not null default 0"
                    else -> " ***not implemented, sry***"
                }
        }
    }

    private val _context: Context? = context
    private val _table: TableInfo = table

    init {
        if (!table.initFinished)
            throw Exception("Table initialization wasn't finished. Call \".finishInit()\" function" +
                " before creating an instance of OpenHelper")
    }

    override fun onCreate(db: SQLiteDatabase) {
        val request = String.format(StrConstants.QUERY_CREATE_TABLE, _table.name,
            _table.columns.select { c -> columnToPartOfInitRequest(c)}.joinToString(", "))
        db.execSQL(request)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("SQLite", "Update from $oldVersion to $newVersion");
        db.execSQL("DROP TABLE IF IT EXISTS " + _table.name)
        onCreate(db)
    }
}