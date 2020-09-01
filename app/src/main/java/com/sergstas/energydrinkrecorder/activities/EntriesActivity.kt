package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.energydrinkrecorder.data.TablesTemplates
import com.sergstas.energydrinkrecorder.fragments.EntriesListFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.toArrayList
import com.sergstas.lib.sql.dbcontrol.DBController
import java.sql.Date

@ExperimentalStdlibApi
class EntriesActivity: AppCompatActivity() {
    private val controller = DBController(this)
    private lateinit var _worker: DBWorker

    init { initDB() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        val data = _worker.getAllEntryInfoInst()
        val grouped = groupByDate(data)
        for (rows in grouped)
            addFragment(rows)
    }

    private fun addFragment(entries: ArrayList<EntryInfo>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("rows", entries)
        val fragment = EntriesListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.entries_list, fragment, "").commit()
    }

    private fun groupByDate(raw: ArrayList<EntryInfo>): ArrayList<ArrayList<EntryInfo>> {
        val sorted = raw.sortedBy { entry -> entry.date }.groupBy { entry -> entry.date }
        val result = ArrayList<ArrayList<EntryInfo>>()
        for (pair in sorted)
            result.add(pair.value.toArrayList())
        return result
    }

    private fun initDB() {
        _worker = DBWorker(controller)
        if (!controller.tryAddTable(TablesTemplates.POSITIONS, MainActivity.POSITIONS_ID) ||
            !controller.tryAddTable(TablesTemplates.ENTRIES, MainActivity.ENTRIES_ID))
            throw ExceptionInInitializerError("Failed to initialize tables")
    }
}