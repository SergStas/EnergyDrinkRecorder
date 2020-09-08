package com.sergstas.energydrinkrecorder.data

import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.activities.MainActivity
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.ENTRIES_ID
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.POSITIONS_ID
import com.sergstas.lib.sql.dbcontrol.DBController

@ExperimentalStdlibApi
abstract class DBHolderActivity: AppCompatActivity() {
    protected val EPSILON = 1e-7

    protected val _controller = DBController(this)
    protected val _worker = DBWorker(_controller)

    init { initDB() }

    private fun initDB() {
        if (!_controller.tryAddTable(TablesTemplates.POSITIONS, POSITIONS_ID) ||
            !_controller.tryAddTable(TablesTemplates.ENTRIES, ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }

    class TablesId {
        companion object {
            val ENTRIES_ID = "entries"
            val POSITIONS_ID = "positions"
        }
    }
}