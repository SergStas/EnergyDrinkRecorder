package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.EntriesListFragment
import com.sergstas.energydrinkrecorder.fragments.EntriesScrollFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import java.lang.Exception
import java.util.HashSet

@ExperimentalStdlibApi
class EntriesActivity: DBHolderActivity() {
    companion object {
        const val ENTRIES_LIST_ARG_KEY = "rows"
    }

    private val _fragments = ArrayList<EntriesScrollFragment>()

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
        bundle.putParcelableArrayList(ENTRIES_LIST_ARG_KEY, entries)
        val fragment = EntriesScrollFragment()
        _fragments.add(fragment)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.entries_list, fragment).commit()
    }

    private fun groupByDate(raw: ArrayList<EntryInfo>): ArrayList<ArrayList<EntryInfo>> {
        val sorted = raw.sortedBy { entry -> entry.date }.groupBy { entry -> entry.date }
        val result = ArrayList<ArrayList<EntryInfo>>()
        for (pair in sorted)
            result.add(pair.value.toArrayList())
        return result
    }

    private fun setRemoveBar() {
        val bar = RemoveBarFragment() //TODO: fix layout
        supportFragmentManager.beginTransaction().add(R.id.entries_removeBarHolder, bar).commit()
        bar.setRemoveIdOnClickListener(View.OnClickListener{
            val id = bar.getSelectedId()
            if (id != null) {
                if (!_worker.tryRemovePosition(id)) //TODO: debug removing from db
                    makeToast()
                else
                    for (fragment in _fragments)
                        if (fragment.tryRemoveFragmentById(id) && fragment.isEmpty())
                            supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        })
        bar.setRemoveAllOnClickListener(View.OnClickListener{}) //TODO: implement
    }

    private fun makeToast() {
        //TODO: implement
    }

    /*private fun reloadFragment(fragment: Fragment, id: Int) {
        val newFragment = EntriesListFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList(ENTRIES_LIST_ARG_KEY, groupByDate(_worker.getAllEntryInfo()).first { list -> list.select { info -> info.entryId }.contains(id) })
        newFragment.arguments = bundle
        supportFragmentManager.beginTransaction().remove(fragment).add(R.id.entries_list, newFragment).commit()
    }*/
}