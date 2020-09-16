package com.sergstas.energydrinkrecorder.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import com.sergstas.extensions.where
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_new_entry.*
import kotlinx.android.synthetic.main.fragment_positions_selector.*

@ExperimentalStdlibApi
class PositionSelectorFragment constructor(private val _context: Context): Fragment() {
    companion object {
        const val POSITIONS_ARG_KEY = "positions"
    }
    private lateinit var _view: View

    private var _name: String? = null
    private var _volume: String? = null
    private var _price: String? = null

    private lateinit var _positions: ArrayList<PositionInfo>
    private var _names = ArrayList<String>()
    private var _volumes = ArrayList<String>()
    private var _prices = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_positions_selector, container, false)
        if (arguments == null || arguments!!.getParcelableArrayList<PositionInfo>(POSITIONS_ARG_KEY) == null)
            throw IllegalArgumentException("Illegal arguments for $this")
        _positions = arguments!!.getParcelableArrayList<PositionInfo>(POSITIONS_ARG_KEY)!!
        _names = _positions.select { p -> p.name!! }.distinct().toArrayList()
        _name = _names.first()
        _view = view
        //setupSpinners()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSpinners()
    }

    fun getSelectedData(): Triple<String, String, String>? {
        return if (_name != null && _volume != null && _price != null)
            Triple(_name!!, _volume!!, _price!!)
        else null
    }

    private fun setupSpinners() {
        setNameSpinner()
        setVolumeSpinner()
        setPriceSpinner()
    }

    private fun setNameSpinner() {
        val adapter = ArrayAdapter(_context, R.layout.support_simple_spinner_dropdown_item, _names)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        posSelector_spinEDName.adapter = adapter
        posSelector_spinEDName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                _name = parent!!.getItemAtPosition(i) as String
                _volumes = _positions.where { pos -> pos.name == _name }
                    .select { pos -> pos.volume.toString() }.distinct().toArrayList()
                setVolumeSpinner()
                _volume = posSelector_spinVolume.getItemAtPosition(0) as String
                _prices = _positions.where { pos -> pos.name == _name && pos.volume.toString() == _volume }
                    .select { pos -> pos.price.toString() }.distinct().toArrayList()
                setPriceSpinner()
                _price = posSelector_spinPrice.getItemAtPosition(0) as String
            }
        }
    }

    private fun setVolumeSpinner() {
        val adapter = ArrayAdapter(_context, R.layout.support_simple_spinner_dropdown_item, _volumes)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        posSelector_spinVolume.adapter = adapter
        posSelector_spinVolume.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                _volume = parent!!.getItemAtPosition(i) as String
                _prices = _positions.where { pos -> pos.name == _name && pos.volume.toString() == _volume }
                    .select { pos -> pos.price.toString() }.distinct().toArrayList()
                setPriceSpinner()
                _price = posSelector_spinPrice.getItemAtPosition(0) as String
            }
        }
    }

    private fun setPriceSpinner() {
        val adapter = ArrayAdapter(_context, R.layout.support_simple_spinner_dropdown_item, _prices)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        posSelector_spinPrice.adapter = adapter
        posSelector_spinPrice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _price = parent!!.getItemAtPosition(position) as String
            }
        }
    }
}