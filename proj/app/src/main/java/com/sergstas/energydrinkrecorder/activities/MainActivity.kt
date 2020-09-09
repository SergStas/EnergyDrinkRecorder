package com.sergstas.energydrinkrecorder.activities

import android.content.Intent
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.ENTRIES_ID
import com.sergstas.extensions.round
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.absoluteValue

//TODO: multithreading
@ExperimentalStdlibApi
class MainActivity : DBHolderActivity() {
    private var _total = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateDataBar()
        setListeners()
    }

    private fun setListeners() {
        statistics_bMore.setOnClickListener {
            val intent = Intent(this, EntriesActivity::class.java)
            startActivity(intent)
        }
        statistics_bSwitch.setOnClickListener {
            _total = !_total
            updateDataBar()
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
        navigation_bGoTo_newPosition.setOnClickListener {
            val intent = Intent(this, NewPositionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateDataBar()
    }

    private fun updateDataBar() {
        val data = if (_total) worker.getTotalStats() else worker.getStatsByDay()
        if (data == null)
            statistics_tData.text = getString(R.string.statistics_tData_failed)
        else
            statistics_tData.text =
                if (data.first.absoluteValue > EPSILON || data.second.absoluteValue > EPSILON)
                    String.format(
                        getString(if (_total) R.string.statistics_tData_total_placeholder
                            else R.string.statistics_tData_daily_placeholder),
                        data.first.round(2),
                        data.second.round(2)
                    )
                else getString(if (_total) R.string.statistics_tData_total_noData
                    else R.string.statistics_tData_daily_noData)
    }
}