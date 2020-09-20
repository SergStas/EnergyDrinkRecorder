package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.os.PersistableBundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.PositionSelectorFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.lib.extensions.handleCrash
import com.sergstas.lib.extensions.tryParseInt
import com.sergstas.lib.toasts.makeDefaultToast
import kotlinx.android.synthetic.main.activity_edit_entry.*

@ExperimentalStdlibApi
class EditEntryActivity: DBHolderActivity() {
    companion object {
        const val POSITIONS_ARG_KEY = "positions"
        const val ENTRY_ARG_KEY = "entry"
    }

    private lateinit var _entry: EntryInfo
    private lateinit var _positions: ArrayList<PositionInfo>
    private lateinit var _posSelector: PositionSelectorFragment

    //TODO: finish
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)
        extractArgs()
        setPositionSelector()
        setListeners()
    }

    private fun extractArgs() {
        handleCrash {
            if (intent.getParcelableExtra<EntryInfo>(ENTRY_ARG_KEY) == null ||
                intent.getParcelableArrayListExtra<PositionInfo>(POSITIONS_ARG_KEY) == null)
                throw IllegalArgumentException("Invalid arguments for $this")
            _entry = intent.getParcelableExtra(ENTRY_ARG_KEY)!!
            _positions = intent.getParcelableArrayListExtra(POSITIONS_ARG_KEY)!!
        }
    }

    //TODO: set values to current
    private fun setPositionSelector() {
        _posSelector = PositionSelectorFragment(this)
        val bundle = Bundle()
        bundle.putParcelableArrayList(PositionSelectorFragment.POSITIONS_ARG_KEY, _positions)
        bundle.putParcelable(PositionSelectorFragment.DEFAULT_POSITION_ARG_KEY, worker.getPosInfoById(_entry.edId))
        _posSelector.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.editEntry_posSelectorHolder, _posSelector).commit()
        setSelectedValues()
    }

    private fun setSelectedValues() {
        editEntry_editCount.setText(_entry.count.toString())
        editEntry_tDate.text = _entry.date
    }

    private fun setListeners() {
        editEntry_bSubmit.setOnClickListener {
            val data = _posSelector.getSelectedData()
            val count = editEntry_editCount.text.toString()
            val date = editEntry_tDate.text.toString()
            if (data == null || !count.tryParseInt().first || date.contains(' '))
                makeDefaultToast(this, getString(R.string.toast_editEntry_submit_fail_incorrectData))
            else {
                val edId = _positions.first {
                    p -> p.price == data.third.toFloat() && p.volume == data.second.toFloat() && p.name == data.first
                }.id
                val newEntry = EntryInfo(-1, edId, data.first, data.second.toFloat(), data.third.toFloat(), count.toInt(), date)
                if (!worker.updateEntry(_entry, newEntry))
                    makeDefaultToast(this, getString(R.string.toast_editEntry_submit_fail_db))
                else {
                    makeDefaultToast(this, getString(R.string.toast_editEntry_submit_success))
                    finish()
                }
            }
        }
    }

    //TODO: calendar
}