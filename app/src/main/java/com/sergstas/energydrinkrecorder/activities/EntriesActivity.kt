package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.EntriesListFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.toArrayList

@ExperimentalStdlibApi
class EntriesActivity: DBHolderActivity() {
    private val _fragments = HashMap<Int, Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        val data = _worker.getAllEntryInfo()
        val grouped = groupByDate(data)
        for (rows in grouped)
            addFragment(rows)
    }

    private fun removePositionFromScreen() {
        supportFragmentManager.beginTransaction().
    }

    private fun addFragment(entries: ArrayList<EntryInfo>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("rows", entries)
        val fragment = EntriesListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.entries_list, fragment, "").commit()
    }

    private fun registerFragments(fragments: ArrayList<EntryInfo>) // TODO: finish removing function

    private fun groupByDate(raw: ArrayList<EntryInfo>): ArrayList<ArrayList<EntryInfo>> {
        val sorted = raw.sortedBy { entry -> entry.date }.groupBy { entry -> entry.date }
        val result = ArrayList<ArrayList<EntryInfo>>()
        for (pair in sorted)
            result.add(pair.value.toArrayList())
        return result
    }

    private fun setRemoveBar() {
        val fragment = RemoveBarFragment()
        fragment.setRemoveIdOnClickListener(run{
            val id = fragment.getSelectedId()
            if (id != null)
                _worker.tryRemovePosition(id)
        })
    }
}