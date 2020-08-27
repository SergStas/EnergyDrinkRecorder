package com.sergstas.energydrinkrecorder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.Tables
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
        val row = Row(Tables.ENTRIES)
        if (_controller.tryAddTable(Tables.ENTRIES, "entries") &&
            row.fill(arrayListOf(null, 1, 2, 3)) &&
            _controller.tryAddPosition(row, "entries")) {
            val extractedRow = _controller.tryGetBy("date", 3, ENTRIES_ID)
            if (extractedRow != null )
                main_tHelloWorld.text = "done"
        }
    }
}