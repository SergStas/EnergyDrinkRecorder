package com.sergstas.energydrinkrecorder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.TablesTemplates
import com.sergstas.lib.sql.dbcontrol.DBController
import com.sergstas.lib.sql.models.Row
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var _controller: DBController
    private val ENTRIES_ID = "entries"
    private val POSITIONS_ID = "positions"

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()
    }

    @ExperimentalStdlibApi
    private fun initDB() {
        _controller = DBController(this)
        if (!_controller.tryAddTable(TablesTemplates.POSITIONS, POSITIONS_ID) ||
            !_controller.tryAddTable(TablesTemplates.ENTRIES, ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }
}