package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.PositionBarFragment
import com.sergstas.energydrinkrecorder.models.PositionInfo

@ExperimentalStdlibApi
class PositionsActivity: DBHolderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)
        val data = _worker.getAllPosInfo()
        addFragments(data)
    }

    private fun addFragments(positions: ArrayList<PositionInfo>) {
        for (pos in positions) {
            val fragment = PositionBarFragment()
            val bundle = Bundle()
            bundle.putParcelable("pos", pos)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.positions_list, fragment, "").commit()
        }
    }
}