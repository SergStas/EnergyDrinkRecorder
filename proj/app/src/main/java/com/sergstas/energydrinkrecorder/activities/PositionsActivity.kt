package com.sergstas.energydrinkrecorder.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.common.Common.Companion.makeToast
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.energydrinkrecorder.fragments.DialogActivity
import com.sergstas.energydrinkrecorder.fragments.DialogActivity.Companion.REMOVE_ALL_REQUEST
import com.sergstas.energydrinkrecorder.fragments.DialogActivity.Companion.REMOVE_ID_REQUEST
import com.sergstas.energydrinkrecorder.fragments.PositionBarFragment
import com.sergstas.energydrinkrecorder.fragments.RemoveBarFragment
import com.sergstas.energydrinkrecorder.models.PositionInfo

@ExperimentalStdlibApi
class PositionsActivity: DBHolderActivity() {
    private val _fragments = HashMap<Int, PositionBarFragment>()

    private var _selectedId = -1

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
                _selectedId = id
                removeId()
            }
        })
        bar.setRemoveAllOnClickListener(View.OnClickListener {
            worker.tryRemoveAllEntries()
            removeAll()
        })
        supportFragmentManager.beginTransaction().add(R.id.positions_removeBarHolder, bar).commit()
    }

    private fun removeId() {
        val intent = Intent(this, DialogActivity::class.java)
        intent.putExtra(DialogActivity.TEXT_ARG_KEY, getString(R.string.dialog_removeId_text))
        intent.putExtra(DialogActivity.ACCEPT_ARG_KEY, getString(R.string.dialog_accept))
        intent.putExtra(DialogActivity.DECLINE_ARG_KEY, getString(R.string.dialog_decline))
        startActivityForResult(intent, REMOVE_ID_REQUEST)}

    private fun removeAll() {
        val intent = Intent(this, DialogActivity::class.java)
        intent.putExtra(DialogActivity.TEXT_ARG_KEY, getString(R.string.dialog_text_removeAll))
        intent.putExtra(DialogActivity.ACCEPT_ARG_KEY, getString(R.string.dialog_accept))
        intent.putExtra(DialogActivity.DECLINE_ARG_KEY, getString(R.string.dialog_decline))
        startActivityForResult(intent, REMOVE_ALL_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REMOVE_ALL_REQUEST -> {
                    if (data!!.getBooleanExtra(DialogActivity.REQUEST_RESULT_KEY, false)) {
                        if (!worker.tryRemoveAllPositions())
                            makeToast(this, getString(R.string.toast_positions_removeAll_fail))
                        else {
                            for (fragment in _fragments) {
                                supportFragmentManager.beginTransaction().remove(fragment.value)
                                    .commit()
                                _fragments.remove(fragment.key)
                            }
                            makeToast(this, getString(R.string.toast_positions_removeAll_success))
                        }
                    }
                }
                REMOVE_ID_REQUEST -> {
                    if (data!!.getBooleanExtra(DialogActivity.REQUEST_RESULT_KEY, false)) {
                        if (!worker.tryRemovePosition(_selectedId))
                            makeToast(this, getString(R.string.toast_positions_removeId_fail))
                        else {
                            val fragment = _fragments[_selectedId]!!
                            supportFragmentManager.beginTransaction().remove(fragment).commit()
                            _fragments.remove(_selectedId)
                            worker.removeRelatedEntries(_selectedId)
                            makeToast(this, getString(R.string.toast_positions_removeId_success))
                        }
                    }
                }
            }
    }
}