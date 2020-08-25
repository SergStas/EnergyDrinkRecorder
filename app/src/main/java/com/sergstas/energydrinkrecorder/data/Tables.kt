package com.sergstas.energydrinkrecorder.data

import android.provider.BaseColumns
import com.sergstas.lib.sql.models.TableInfo

class Tables {
    companion object {
        val POSITIONS = TableInfo("pos")
        val ENTRIES = TableInfo("entries")

        init {
            POSITIONS.addColumn(BaseColumns._ID, Int::class, true)
            POSITIONS.addColumn("name", String::class)
            POSITIONS.addColumn("volume", Float::class)
            POSITIONS.addColumn("price", Float::class)
            POSITIONS.finishInit()

            ENTRIES.addColumn(BaseColumns._ID, Int::class, true)
            ENTRIES.addColumn("edId", Int::class)
            ENTRIES.addColumn("count", Int::class)
            ENTRIES.addColumn("date", String::class)
            ENTRIES.finishInit()
        }
    }
}