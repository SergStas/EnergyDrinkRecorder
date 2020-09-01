package com.sergstas.energydrinkrecorder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.energydrinkrecorder.data.TablesTemplates
import com.sergstas.extensions.round
import com.sergstas.lib.sql.dbcontrol.DBController
import com.sergstas.lib.sql.models.Row
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.absoluteValue

@ExperimentalStdlibApi
class MainActivity : AppCompatActivity() {
    companion object {
        val ENTRIES_ID = "entries"
        val POSITIONS_ID = "positions"

        private val EPSILON = 1e-7
    }

    private lateinit var _controller: DBController
    private lateinit var _worker: DBWorker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()
        //reloadDataExample()
        setDataBar()
        setListeners()
    }

    private fun setListeners() {
        statistics_bMore.setOnClickListener {
            val intent = Intent(this, EntriesActivity::class.java)
            //TODO: pass controller
            startActivity(intent)
        }
    }

    private fun setDataBar() {
        val daily = _worker.getStatsByDay()
        if (daily == null)
            statistics_tData.text = getString(R.string.statistics_tData_failed)
        else
            statistics_tData.text =
                if (daily.first.absoluteValue > EPSILON && daily.second.absoluteValue > EPSILON)
                    String.format(getString(R.string.statistics_tData_today_placeholder), daily.first.round(2), daily.second.round(2))
                else getString(R.string.statistics_tData_today_noData)
    }

    private fun initDB() {
        _controller = DBController(this)
        _worker = DBWorker(_controller)
        if (!_controller.tryAddTable(TablesTemplates.POSITIONS, POSITIONS_ID) ||
            !_controller.tryAddTable(TablesTemplates.ENTRIES, ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }

    private fun reloadDataExample() {
        val cl1 = _controller.tryClear(POSITIONS_ID)
        val cl2 = _controller.tryClear(ENTRIES_ID)

        val monster = Row(_controller.getTable(POSITIONS_ID)!!)
        monster.fill(arrayListOf(null, "Monster", 0.45f, 66f))
        val tornado = Row(_controller.getTable(POSITIONS_ID)!!)
        tornado.fill(arrayListOf(null, "Tornado Energy", 0.45f, 34f))
        _controller.tryAddPosition(POSITIONS_ID, monster)
        _controller.tryAddPosition(POSITIONS_ID, tornado)

        val entry = Row(_controller.getTable(ENTRIES_ID)!!)
        entry.fill(arrayListOf(null, _worker.getEDIdByName("Monster"), 1, "2020-08-30"))
        _controller.tryAddPosition(ENTRIES_ID, entry)
        entry.fill(arrayListOf(null, _worker.getEDIdByName("Tornado Energy"), 1, "2020-08-30"))
        _controller.tryAddPosition(ENTRIES_ID, entry)
        entry.fill(arrayListOf(null, _worker.getEDIdByName("Tornado Energy"), 2, "2020-08-31"))
        _controller.tryAddPosition(ENTRIES_ID, entry)
        entry.fill(arrayListOf(null, _worker.getEDIdByName("Monster"), 2, "2020-09-01"))
        _controller.tryAddPosition(ENTRIES_ID, entry)
    }
}