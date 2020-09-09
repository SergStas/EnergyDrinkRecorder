package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.view.View
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.common.Common.Companion.makeToast
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.PositionBarFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.PositionInfo

@ExperimentalStdlibApi
class PositionsActivity: DBHolderActivity() {
    private val _fragments = HashMap<Int, PositionBarFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)
        val data = worker.getAllPosInfo()
        addFragments(data)
        setRemoveBar()
    }

    private fun addFragments(positions: ArrayList<PositionInfo>) {
        for (pos in positions) {
            val fragment = PositionBarFragment()
            val bundle = Bundle()
            bundle.putParcelable("pos", pos)
            fragment.arguments = bundle
            _fragments[pos.id] = fragment
            supportFragmentManager.beginTransaction().add(R.id.positions_list, fragment, "").commit()
        }
    }

    private fun setRemoveBar() {
        val bar = RemoveBarFragment()
        bar.setRemoveIdOnClickListener(View.OnClickListener {
            val id = bar.getSelectedId()
            if (id != null) {
                if (!worker.tryRemovePosition(id))
                    makeToast(this, getString(R.string.toast_positions_removeId_fail))
                else {
                    supportFragmentManager.beginTransaction().remove(_fragments[id]!!)
                    _fragments.remove(id)
                    //worker.tryRemoveAllPositions() TODO: finish
                    makeToast(this, getString(R.string.toast_positions_removeId_success))
                }
            }
        })
        bar.setRemoveAllOnClickListener(View.OnClickListener {
            //worker.tryRemoveAllPositions()
            removeAll()
        })
        supportFragmentManager.beginTransaction().add(R.id.positions_removeBarHolder, bar).commit()
    }

    private fun removeAll() {
        if (!worker.tryRemoveAllPositions())
            makeToast(this, getString(R.string.toast_positions_removeAll_fail))
        else {
            for (fragment in _fragments) {
                supportFragmentManager.beginTransaction().remove(fragment.value).commit()
                _fragments.remove(fragment.key)
            }
            makeToast(this, getString(R.string.toast_positions_removeAll_success))
        }
    }
}