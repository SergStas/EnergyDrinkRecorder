package com.sergstas.energydrinkrecorder.data

import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.activities.MainActivity
import com.sergstas.lib.sql.dbcontrol.DBController

@ExperimentalStdlibApi
abstract class DBHolderActivity: AppCompatActivity() {
    protected val controller = DBController(this)
    protected val worker = DBWorker(controller)

    init { initDB() }

    private fun initDB() {
        if (!controller.tryAddTable(TablesTemplates.POSITIONS, MainActivity.POSITIONS_ID) ||
            !controller.tryAddTable(TablesTemplates.ENTRIES, MainActivity.ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }
}