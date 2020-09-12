package com.sergstas.energydrinkrecorder.data

import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.ENTRIES_ID
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.POSITIONS_ID
import com.sergstas.lib.sql.dbcontrol.DBController

@ExperimentalStdlibApi
abstract class DBHolderActivity: AppCompatActivity() {
    protected val EPSILON = 1e-7

    protected val controller = DBController(this)
    protected val worker = DBWorker(controller)

    init { initDB() }

    private fun initDB() {
        if (!controller.addTable(TablesTemplates.POSITIONS, POSITIONS_ID) ||
            !controller.addTable(TablesTemplates.ENTRIES, ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }

    class TablesId {
        companion object {
            val ENTRIES_ID = "entries"
            val POSITIONS_ID = "positions"
        }
    }
}