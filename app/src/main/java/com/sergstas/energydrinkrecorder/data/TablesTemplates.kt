package com.sergstas.energydrinkrecorder.data

import android.provider.BaseColumns
import com.sergstas.lib.sql.models.TableInfo

class TablesTemplates {
    companion object {
        val POSITIONS = TableInfo("pos")
        val ENTRIES = TableInfo("entries")

        init {
            POSITIONS.addColumn("_id", Int::class, true)
            POSITIONS.addColumn("name", String::class)
            POSITIONS.addColumn("volume", Float::class)
            POSITIONS.addColumn("price", Float::class)
            POSITIONS.finishInit()

            ENTRIES.addColumn("_id", Int::class, true)
            ENTRIES.addColumn("edId", Int::class)
            ENTRIES.addColumn("count", Int::class)
            ENTRIES.addColumn("date", Int::class)
            ENTRIES.finishInit()
        }
    }
}