package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import com.sergstas.extensions.where
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.sql.Date
import kotlin.collections.ArrayList


@ExperimentalStdlibApi
class NewEntryActivity: DBHolderActivity() {
    private lateinit var _positions: ArrayList<PositionInfo>
    private var _names: ArrayList<String> = arrayListOf("")
    private var _volumes: ArrayList<String> = arrayListOf("")
    private var _prices: ArrayList<String> = arrayListOf("")

    private var _selectedName: String? = null
    private var _selectedVolume: String? = null
    private var _selectedPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)
        _positions = intent.getParcelableArrayListExtra<PositionInfo>("positions")!!
        _names = _positions.select { p -> p.name!! }.distinct().toArrayList()
        _selectedName = _positions.select { p -> p.name }.first()
        setListener()
        setupSpinners()
    }

    private fun setupSpinners() {
        setNameSpinner()
        setVolumeSpinner()
        setPriceSpinner()
    }

    private fun setNameSpinner() {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, _names)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        newEntry_spinEDName.adapter = adapter
        newEntry_spinEDName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                _selectedName = parent!!.getItemAtPosition(i) as String
                _volumes = _positions.where { pos -> pos.name == _selectedName }
                    .select { pos -> pos.volume.toString() }.distinct().toArrayList()
                setVolumeSpinner()
                _selectedVolume = newEntry_spinVolume.getItemAtPosition(0) as String
                _prices = _positions.where { pos -> pos.name == _selectedName && pos.volume.toString() == _selectedVolume }
                    .select { pos -> pos.price.toString() }.distinct().toArrayList()
                setPriceSpinner()
                _selectedPrice = newEntry_spinPrice.getItemAtPosition(0) as String
            }
        }
    }

    private fun setVolumeSpinner() {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, _volumes)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        newEntry_spinVolume.adapter = adapter
        newEntry_spinVolume.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                _selectedVolume = parent!!.getItemAtPosition(i) as String
                _prices = _positions.where { pos -> pos.name == _selectedName && pos.volume.toString() == _selectedVolume }
                    .select { pos -> pos.price.toString() }.distinct().toArrayList()
                setPriceSpinner()
                _selectedPrice = newEntry_spinPrice.getItemAtPosition(0) as String
            }
        }
    }

    private fun setPriceSpinner() {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, _prices)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        newEntry_spinPrice.adapter = adapter
        newEntry_spinPrice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _selectedPrice = parent!!.getItemAtPosition(position) as String
            }
        }
    }

    private fun setListener() {
        newEntry_bSubmit.setOnClickListener {
            if (_selectedName != null && _selectedVolume != null && _selectedPrice != null) {
                val edId =
                    _positions.first { p -> p.name == _selectedName && p.volume == _selectedVolume!!.toFloat() && p.price == _selectedPrice!!.toFloat() }.id
                val count = newEntry_editCount.text.toString().toInt()
                val date = Date(System.currentTimeMillis()).toString()
                worker.addNewEntry(edId, count, date)
                finish()
            }
        }
    }
}