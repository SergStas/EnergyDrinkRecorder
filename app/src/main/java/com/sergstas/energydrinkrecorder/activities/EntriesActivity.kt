package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.energydrinkrecorder.data.TablesTemplates
import com.sergstas.energydrinkrecorder.fragments.EntriesListFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.toArrayList
import com.sergstas.lib.sql.dbcontrol.DBController

@ExperimentalStdlibApi
class EntriesActivity: DBHolderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        val data = worker.getAllEntryInfo()
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
}