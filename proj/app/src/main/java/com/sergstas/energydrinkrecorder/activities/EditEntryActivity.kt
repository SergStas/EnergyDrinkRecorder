package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.os.PersistableBundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.PositionSelectorFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.lib.extensions.handleCrash
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
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_edit_entry)
        extractArgs()
        setPositionSelector()
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
        _posSelector.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.editEntry_posSelectorHolder, _posSelector).commit()
    }

    private fun setListeners() {
        editEntry_bSubmit.setOnClickListener {
            //TODO: implement
        }
    }

    //TODO: calendar
}