package com.sergstas.energydrinkrecorder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.activities.EntriesActivity
import com.sergstas.energydrinkrecorder.models.EntryInfo
import kotlinx.android.synthetic.main.fragment_entrieslist.view.*

@ExperimentalStdlibApi
class EntriesScrollFragment: Fragment() {
    companion object {
        const val ENTRY_BAR_ARG_KEY = "entryInfo"
    }
    private val _idDictionary = HashMap<Int, Fragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_entrieslist, container, false)
        if (arguments!!.getParcelableArrayList<EntryInfo>(EntriesActivity.ENTRIES_LIST_ARG_KEY) == null)
            throw IllegalArgumentException("Invalid bundle for $this")
        val rows = arguments!!.getParcelableArrayList<EntryInfo>(EntriesActivity.ENTRIES_LIST_ARG_KEY)!!
        if (rows.count() == 0)
            return view
        processEntriesList(rows, view)
        view.entriesList_date.text = rows.first().date!!
        return view
    }

    fun tryRemoveFragmentById(id: Int): Boolean {
        if (!_idDictionary.containsKey(id))
            return false
        childFragmentManager.beginTransaction().remove(_idDictionary[id]!!).commit()
        _idDictionary.remove(id)
        return true
    }

    fun isEmpty(): Boolean {
        return _idDictionary.isEmpty()
    }

    private fun processEntriesList(rows: ArrayList<EntryInfo>, view: View) {
        rows.forEach { entryInfo ->
            val fragment = createEntryBarFragment(entryInfo)
            childFragmentManager.beginTransaction().add(R.id.entriesList_list, fragment).commit()
        }
    }

    private fun createEntryBarFragment(entry: EntryInfo): Fragment {
        val fragment = EntryBarFragment()
        _idDictionary[entry.entryId] = fragment
        val bundle = Bundle()
        bundle.putParcelable(ENTRY_BAR_ARG_KEY, entry)
        fragment.arguments = bundle
        return fragment
    }
}