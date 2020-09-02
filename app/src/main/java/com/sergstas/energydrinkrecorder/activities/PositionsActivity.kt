package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.energydrinkrecorder.data.TablesTemplates
import com.sergstas.energydrinkrecorder.fragments.PositionBarFragment
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.lib.sql.dbcontrol.DBController

@ExperimentalStdlibApi
class PositionsActivity: AppCompatActivity() {
    private val _controller = DBController(this)
    private lateinit var _worker: DBWorker

    init { initDB() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)
        val data = _worker.getAllPosInfo()
        addFragments(data)
    }

    private fun addFragments(positions: ArrayList<PositionInfo>) {
        for (pos in positions) {
            val fragment = PositionBarFragment()
            val bundle = Bundle()
            bundle.putParcelable("pos", pos)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.positions_list, fragment, "").commit()
        }
    }

    private fun initDB() {
        _worker = DBWorker(_controller)
        if (!_controller.tryAddTable(TablesTemplates.POSITIONS, MainActivity.POSITIONS_ID) ||
            !_controller.tryAddTable(TablesTemplates.ENTRIES, MainActivity.ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }
}