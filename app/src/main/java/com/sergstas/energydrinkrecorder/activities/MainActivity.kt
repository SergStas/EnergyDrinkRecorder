package com.sergstas.energydrinkrecorder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.extensions.round
import com.sergstas.lib.sql.dbcontrol.DBController
import com.sergstas.lib.sql.models.Row
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.absoluteValue

@ExperimentalStdlibApi
class MainActivity : DBHolderActivity() {
    companion object {
        val ENTRIES_ID = "entries"
        val POSITIONS_ID = "positions"

        private val EPSILON = 1e-7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //reloadDataExample()
        setDataBar()
        setListeners()
    }

    private fun setListeners() {
        statistics_bMore.setOnClickListener {
            val intent = Intent(this, EntriesActivity::class.java)
            startActivity(intent)
        }
        navigation_bGoTo_positionsList.setOnClickListener {
            val intent = Intent(this, PositionsActivity::class.java)
            startActivity(intent)
        }
        navigation_bGoTo_newEntry.setOnClickListener {
            val intent = Intent(this, NewEntryActivity::class.java)
            intent.putExtra("positions", worker.getAllPosInfo())
            startActivity(intent)
        }
    }

    private fun setDataBar() {
        val daily = worker.getStatsByDay()
        if (daily == null)
            statistics_tData.text = getString(R.string.statistics_tData_failed)
        else
            statistics_tData.text =
                if (daily.first.absoluteValue > EPSILON && daily.second.absoluteValue > EPSILON)
                    String.format(
                        getString(R.string.statistics_tData_today_placeholder),
                        daily.first.round(2),
                        daily.second.round(2)
                    )
                else getString(R.string.statistics_tData_today_noData)
    }

    private fun reloadDataExample() {
        val cl1 = controller.tryClear(POSITIONS_ID)
        val cl2 = controller.tryClear(ENTRIES_ID)

        val monster = Row(controller.getTable(POSITIONS_ID)!!)
        monster.fill(arrayListOf(null, "Monster", 0.45f, 66f))
        val tornado = Row(controller.getTable(POSITIONS_ID)!!)
        tornado.fill(arrayListOf(null, "Tornado Energy", 0.45f, 34f))
        controller.tryAddPosition(POSITIONS_ID, monster)
        controller.tryAddPosition(POSITIONS_ID, tornado)

        val entry = Row(controller.getTable(ENTRIES_ID)!!)
        entry.fill(arrayListOf(null, worker.getEDIdByName("Monster"), 1, "2020-08-30"))
        controller.tryAddPosition(ENTRIES_ID, entry)
        entry.fill(arrayListOf(null, worker.getEDIdByName("Tornado Energy"), 1, "2020-08-30"))
        controller.tryAddPosition(ENTRIES_ID, entry)
        entry.fill(arrayListOf(null, worker.getEDIdByName("Tornado Energy"), 2, "2020-08-31"))
        controller.tryAddPosition(ENTRIES_ID, entry)
        entry.fill(arrayListOf(null, worker.getEDIdByName("Monster"), 2, "2020-09-01"))
        controller.tryAddPosition(ENTRIES_ID, entry)
    }
}