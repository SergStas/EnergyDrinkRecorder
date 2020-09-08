package com.sergstas.energydrinkrecorder.activities

import android.content.Intent
import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.extensions.round
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.absoluteValue

@ExperimentalStdlibApi
class MainActivity : DBHolderActivity() {
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

    private fun updateDataBar() { //TODO: total statistics
        val daily = worker.getStatsByDay()
        if (daily == null)
            statistics_tData.text = getString(R.string.statistics_tData_failed)
        else
            statistics_tData.text =
                if (daily.first.absoluteValue > EPSILON || daily.second.absoluteValue > EPSILON)
                    String.format(
                        getString(R.string.statistics_tData_today_placeholder),
                        daily.first.round(2),
                        daily.second.round(2)
                    )
                else getString(R.string.statistics_tData_today_noData)
    }
}