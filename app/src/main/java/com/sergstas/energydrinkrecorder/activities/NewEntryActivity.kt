package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import kotlinx.android.synthetic.main.activity_new_entry.*


@ExperimentalStdlibApi
class NewEntryActivity: AppCompatActivity() {
    private lateinit var _positions: ArrayList<PositionInfo>
    private var _selectedPositions: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)
        _positions = intent.getParcelableArrayListExtra<PositionInfo>("positions")!!
        _selectedPositions = _positions.select { p -> p.name }.first()
        setupSpinner()
        setListener()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, _positions.select { p -> p.name }.toArrayList())
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        newEntry_spinEDName.adapter = adapter
        newEntry_spinEDName.setOnItemClickListener { parent, _, i, _ ->
            _selectedPositions = parent.getItemAtPosition(i) as String
        }
    }

    private fun setListener() {
        newEntry_bSubmit.setOnClickListener {
            val count = Integer.parseInt(newEntry_editCount.text.toString())
            val selected = _positions.first { p -> p.name == _selectedPositions }
            val result = PositionInfo(selected.id)
            //TODO: finish
        }
    }
}