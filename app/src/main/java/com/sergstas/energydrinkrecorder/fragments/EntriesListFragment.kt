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
import kotlinx.android.synthetic.main.fragment_entrybar.view.*

@ExperimentalStdlibApi
class EntriesListFragment /*constructor(private val _entries: ArrayList<EntryInfo>, private val _context: Context)*/: ListFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dayslist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = DaysListAdapter(activity!!.applicationContext, R.layout.fragment_entrybar, /*_entries*/ emptyList())
        listAdapter = adapter
    }

    @ExperimentalStdlibApi
    private class DaysListAdapter(context: Context, textViewResourceId: Int, objects: List<EntryInfo>)
        : ArrayAdapter<EntryInfo>(context, textViewResourceId, objects) {
        private val _context = context
        private val _entryInfo = objects
        private val _view = textViewResourceId

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val bar = inflater.inflate(_view, parent, false)
            val row = _entryInfo[position]
            if (row.isFilled) {
                bar.entryBar_id.text = row.entryId.toString()
                bar.entryBar_name.text = row.edName
                bar.entryBar_volume.text = row.volume!!.round(2).toString()
                bar.entryBar_price.text = row.price!!.round(2).toString()
                bar.entryBar_count.text = row.count.toString()
            }
            return bar
        }
    }
}