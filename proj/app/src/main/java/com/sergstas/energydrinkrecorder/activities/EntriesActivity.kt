package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.view.View
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.common.Common.Companion.makeToast
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.EntriesScrollFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.toArrayList

@ExperimentalStdlibApi
class EntriesActivity: DBHolderActivity() {
    companion object {
        const val ENTRIES_LIST_ARG_KEY = "rows"
    }

    private val _fragments = ArrayList<EntriesScrollFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        val data = worker.getAllEntryInfo()
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
        supportFragmentManager.beginTransaction().add(R.id.entries_listHolder, fragment).commit()
    }

    private fun groupByDate(raw: ArrayList<EntryInfo>): ArrayList<ArrayList<EntryInfo>> {
        val sorted = raw.sortedBy { entry -> entry.date }.groupBy { entry -> entry.date }
        val result = ArrayList<ArrayList<EntryInfo>>()
        for (pair in sorted)
            result.add(pair.value.toArrayList())
        return result
    }

    private fun setRemoveBar() {
        val bar = RemoveBarFragment()
        supportFragmentManager.beginTransaction().add(R.id.entries_removeBarHolder, bar).commit()
        bar.setRemoveIdOnClickListener(View.OnClickListener {
            val id = bar.getSelectedId()
            if (id != null) {
                if (!worker.tryRemoveEntry(id))
                    makeToast(this, getString(R.string.toast_entries_removeId_fail))
                else {
                    for (fragment in _fragments)
                        if (fragment.tryRemoveFragmentById(id) && fragment.isEmpty())
                            supportFragmentManager.beginTransaction().remove(fragment).commit()
                    makeToast(this, getString(R.string.toast_entries_removeId_success))
                }
            }
        })
        bar.setRemoveAllOnClickListener(View.OnClickListener{
            removeAll()
        })
    }

    private fun removeAll() {
        if (!worker.tryRemoveAllEntries())
            makeToast(this, getString(R.string.toast_entries_clearAll_failed))
        else {
            for (fragment in _fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
                _fragments.remove(fragment)
            }
            makeToast(this, getString(R.string.toast_entries_removeAll_success))
        }
    }
}