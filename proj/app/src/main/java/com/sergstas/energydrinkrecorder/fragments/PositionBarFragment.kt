package com.sergstas.energydrinkrecorder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.models.PositionInfo
import kotlinx.android.synthetic.main.fragment_positionbar.view.*

@ExperimentalStdlibApi
class PositionBarFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_positionbar, container, false)
        if (arguments != null) {
            val pos = arguments!!.getParcelable<PositionInfo>("pos")!!
            view.positionBar_id.text = pos.id.toString()
            view.positionBar_name.text = pos.name
            view.positionBar_volume.text = pos.volume.toString()
            view.positionBar_price.text = pos.price.toString()
        }
        return view
    }
}