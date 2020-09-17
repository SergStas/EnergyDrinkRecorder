package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBHolderActivity
import com.sergstas.lib.toasts.makeDefaultToast
import kotlinx.android.synthetic.main.activity_new_position.*
import java.lang.Exception

@ExperimentalStdlibApi
class NewPositionActivity: DBHolderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_position)
        setListeners()
    }

    private fun setListeners() {
        newPosition_bSubmit.setOnClickListener {
            try {
                val name = newPosition_editName.text.toString()
                val volume = newPosition_editVolume.text.toString().toFloat()
                val price = newPosition_editPrice.text.toString().toFloat()
                worker.addNewPosition(name, volume, price)
                makeDefaultToast(this, getString(R.string.toast_newPosition_success))
                finish()
            } catch (e: Exception) {
                makeDefaultToast(this, getString(R.string.toast_newPosition_fail))
            }
        }
    }
}