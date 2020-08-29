package com.sergstas.energydrinkrecorder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.energydrinkrecorder.data.TablesTemplates
import com.sergstas.extensions.round
import com.sergstas.lib.sql.dbcontrol.DBController
import com.sergstas.lib.sql.models.Row
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.sql.Date
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    companion object {
        val ENTRIES_ID = "entries"
        val POSITIONS_ID = "positions"

        private val EPSILON = 1e-7
    }

    private lateinit var _controller: DBController
    private lateinit var _worker: DBWorker

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()
        val daily = _worker.getStatsByDay()
        if (daily == null)
            statistics_tData.text = getString(R.string.statistics_tData_failed)
        else
            statistics_tData.text =
                 if (daily.first.absoluteValue > EPSILON && daily.second.absoluteValue > EPSILON)
                     String.format(getString(R.string.statistics_tData_today_placeholder), daily.first.round(2), daily.second.round(2))
                 else getString(R.string.statistics_tData_today_noData)
    }

    @ExperimentalStdlibApi
    private fun initDB() {
        _controller = DBController(this)
        _worker = DBWorker(_controller)
        if (!_controller.tryAddTable(TablesTemplates.POSITIONS, POSITIONS_ID) ||
            !_controller.tryAddTable(TablesTemplates.ENTRIES, ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }
}