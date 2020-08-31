package com.sergstas.energydrinkrecorder.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.ListFragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.extensions.toArrayList

@ExperimentalStdlibApi
class DaysListFragment: ListFragment() {
    private var _data: ArrayList<EntryInfo>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments != null)
            _data = arguments!!.getParcelableArrayList<EntryInfo>("rows")
        return inflater.inflate(R.layout.fragment_entrieslist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listAdapter = DaysListAdapter(activity!!.applicationContext, R.layout.fragment_entrieslist, transformData(_data!!))
    }

    private fun transformData(raw: ArrayList<EntryInfo>): List<List<EntryInfo>> {
        val sorted = raw.sortedBy { entry -> entry.date }.groupBy { entry -> entry.date }
        val result = ArrayList<List<EntryInfo>>()
        for (pair in sorted)
            result.add(pair.value)
        return result.toList()
    }

    private class DaysListAdapter(context: Context, viewId: Int, data: List<List<EntryInfo>>)
        : ArrayAdapter<List<EntryInfo>>(context, viewId, data) {
        private val _context = context
        private val _entries = data
        private val _view = viewId

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //val entriesList = inflater.inflate(_view, parent, false)
            //entriesList.
            val entries = _entries[position]
            val fragment = EntriesListFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("entries", entries.toArrayList())
            fragment.arguments = bundle
            val view = fragment.onCreateView(inflater, parent, null)!!
            //supportFragmentManager.beginTransaction().add(R.id.activity_entries_root, fragment, "").commit()
            return
        }
    }
}