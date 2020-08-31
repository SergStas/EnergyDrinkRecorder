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
import com.sergstas.extensions.round
import kotlinx.android.synthetic.main.fragment_entrieslist.view.*
import kotlinx.android.synthetic.main.fragment_entrybar.view.*
import java.util.ArrayList

@ExperimentalStdlibApi
class EntriesListFragment: ListFragment() {
    private var _rows: ArrayList<EntryInfo>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _rows = arguments!!.getParcelableArrayList<EntryInfo>("entries")
        val view = inflater.inflate(R.layout.fragment_entrieslist, container, false)
        view.entriesList_date.text = _rows!!.first().date
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = EntriesListAdapter(activity!!.applicationContext, R.layout.fragment_entrybar, _rows!!.toList())
        listAdapter = adapter
    }

    private class EntriesListAdapter(context: Context, viewId: Int, data: List<EntryInfo>)
        : ArrayAdapter<EntryInfo>(context, viewId, data) {
        private val _context = context
        private val _entriesInfo = data
        private val _view = viewId

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bar = inflater.inflate(_view, parent, false)
            val row = _entriesInfo[position]
            bar.entryBar_id.text = row.entryId.toString()
            if (row.isFilled) {
                bar.entryBar_name.text = row.edName
                bar.entryBar_volume.text = row.volume!!.round(2).toString()
                bar.entryBar_price.text = row.price!!.round(2).toString()
                bar.entryBar_count.text = row.count.toString()
            }
            return bar
        }
    }
}