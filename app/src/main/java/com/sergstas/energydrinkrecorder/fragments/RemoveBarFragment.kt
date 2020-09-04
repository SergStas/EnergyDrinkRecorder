package com.sergstas.energydrinkrecorder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import kotlinx.android.synthetic.main.fragment_remove_bar.*
import kotlinx.android.synthetic.main.fragment_remove_bar.view.*
import java.lang.Exception

class RemoveBarFragment: Fragment() {
    private lateinit var _removeIdUnit: Unit
    private lateinit var _removeAllUnit: Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_remove_bar, container, false)
        view.removeBar_bRemoveId.setOnClickListener { _removeIdUnit }
        view.removeBar_bRemoveAll.setOnClickListener { _removeAllUnit }
        return view
    }

    fun setRemoveIdOnClickListener(listener: Unit) {
        _removeIdUnit = listener
    }

    fun setRemoveAllOnClickListener(listener: Unit) {
        _removeAllUnit = listener
    }

    fun getSelectedId(): Int? {
        return try {
            removeBar_editId.text.toString().toInt()
        }
        catch (e: Exception) {
            null
        }
    }
}