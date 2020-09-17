package com.sergstas.energydrinkrecorder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.lib.extensions.round
import kotlinx.android.synthetic.main.fragment_entrybar.view.*

@ExperimentalStdlibApi
class EntryBarFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_entrybar, container, false)
        if (arguments!!.getParcelable<EntryInfo>(EntriesScrollFragment.ENTRY_BAR_ARG_KEY) == null)
            throw IllegalArgumentException("Invalid bundle for $this")
        val entry = arguments!!.getParcelable<EntryInfo>(EntriesScrollFragment.ENTRY_BAR_ARG_KEY)!!
        fillTextViews(view, entry)
        return view
    }

    private fun fillTextViews(view: View, entry: EntryInfo) {
        if (!entry.isFilled)
            throw IllegalArgumentException("Invalid EntryInfo argument for $this")
        view.entryBar_id.text = entry.entryId.toString()
        view.entryBar_name.text = entry.edName
        view.entryBar_volume.text = entry.volume!!.round(2).toString()
        view.entryBar_price.text = entry.price!!.round(2).toString()
        view.entryBar_count.text = entry.count!!.toString()
    }
}