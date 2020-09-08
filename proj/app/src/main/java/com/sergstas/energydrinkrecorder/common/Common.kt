package com.sergstas.energydrinkrecorder.common

import android.content.Context
import android.widget.Toast

class Common {
    companion object {
        fun makeToast(context: Context, content: String) {
            val toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}