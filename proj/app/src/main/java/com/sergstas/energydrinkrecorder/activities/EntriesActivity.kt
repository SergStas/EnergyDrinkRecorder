package com.sergstas.energydrinkrecorder.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.EntriesScrollFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.lib.extensions.select
import com.sergstas.lib.extensions.toArrayList
import com.sergstas.lib.toasts.makeDefaultToast

@ExperimentalStdlibApi
class EntriesActivity: DBHolderActivity() {
    private val _fragments = ArrayList<EntriesScrollFragment>()

    private lateinit var _entries: ArrayList<EntryInfo>
    private lateinit var _positions: ArrayList<PositionInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        _entries = worker.getAllEntryInfo()
        _positions = worker.getAllPosInfo()
        val grouped = groupByDate(_entries)
        for (rows in grouped)
            addFragment(rows)
        setRemoveBar()
    }

    private fun addFragment(entries: ArrayList<EntryInfo>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(EntriesScrollFragment.ENTRIES_ARG_KEY, entries)
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
        bar.removeIdListener = View.OnClickListener {
            val id = bar.getSelectedId()
            if (id != null) {
                if (!worker.removeEntry(id))
                    makeDefaultToast(this, getString(R.string.toast_entries_removeId_fail))
                else {
                    for (fragment in _fragments)
                        if (fragment.tryRemoveFragmentById(id) && fragment.isEmpty())
                            supportFragmentManager.beginTransaction().remove(fragment).commit()
                    makeDefaultToast(this, getString(R.string.toast_entries_removeId_success))
                }
            }
        }
        bar.editIdListener = View.OnClickListener {
            val id = bar.getSelectedId()
            if (id != null) {
                editId(id) //TODO: debug
            }
        }
        bar.removeAllListener = View.OnClickListener{
            removeAll()
        }
    }

    private fun editId(id: Int) {
        if (!_entries.select { e -> e.entryId }.contains(id))
            makeDefaultToast(this, getString(R.string.toast_entries_editId_fail_notFound))
        else {
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra(EditEntryActivity.POSITIONS_ARG_KEY, _positions)
            intent.putExtra(EditEntryActivity.ENTRY_ARG_KEY, _entries.first { e -> e.entryId == id})
            startActivity(intent)
        }
    }

    private fun removeAll() {
        if (!worker.removeAllEntries())
            makeDefaultToast(this, getString(R.string.toast_entries_clearAll_failed))
        else {
            for (fragment in _fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
                _fragments.remove(fragment)
            }
            makeDefaultToast(this, getString(R.string.toast_entries_removeAll_success))
        }
    }
}