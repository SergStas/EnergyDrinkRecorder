package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.EntriesListFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import kotlinx.android.synthetic.main.activity_entries.*
import kotlinx.android.synthetic.main.fragment_entrieslist.*
import java.util.HashSet

@ExperimentalStdlibApi
class EntriesActivity: DBHolderActivity() {
    private val _ids = HashMap<HashSet<Int>, Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        val data = _worker.getAllEntryInfo()
        val grouped = groupByDate(data)
        for (rows in grouped)
            addFragment(rows)
        setRemoveBar()
    }

    private fun addFragment(entries: ArrayList<EntryInfo>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("rows", entries)
        val fragment = EntriesListFragment()
        var set = HashSet(entries.select { info -> info.entryId }.toArrayList())
        _ids[set] = fragment
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

    private fun setRemoveBar() { //TODO: debug, refactor, finish
        val bar = RemoveBarFragment()
        bar.setRemoveIdOnClickListener(run{
            val id = bar.getSelectedId()
            if (id != null) {
                _worker.tryRemovePosition(id)
                val set = _ids.keys.first { set -> set.contains(id) }
                val fragment = _ids[set]
                _ids.remove(set)
                reloadFragment(fragment!!, id)
            }
        })
        supportFragmentManager.beginTransaction().add(R.id.entries_removeBarHolder, bar).commit()
    }

    private fun reloadFragment(fragment: Fragment, id: Int) {
        val newFragment = EntriesListFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("rows", groupByDate(_worker.getAllEntryInfo()).first { list -> list.select { info -> info.entryId }.contains(id) })
        newFragment.arguments = bundle
        supportFragmentManager.beginTransaction().remove(fragment).add(R.id.entries_list, newFragment).commit()
    }
}