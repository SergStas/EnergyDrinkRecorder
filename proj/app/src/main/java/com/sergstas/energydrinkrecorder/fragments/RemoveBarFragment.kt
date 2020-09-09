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

//TODO: assert
class RemoveBarFragment: Fragment() {
    private lateinit var _removeIdListener: View.OnClickListener
    private lateinit var _removeAllListener: View.OnClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_remove_bar, container, false)
        view.removeBar_bRemoveId.setOnClickListener(_removeIdListener)
        view.removeBar_bRemoveAll.setOnClickListener(_removeAllListener)
        return view
    }

    fun setRemoveIdOnClickListener(listener: View.OnClickListener) {
        _removeIdListener = listener
    }

    fun setRemoveAllOnClickListener(listener: View.OnClickListener) {
        _removeAllListener = listener
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