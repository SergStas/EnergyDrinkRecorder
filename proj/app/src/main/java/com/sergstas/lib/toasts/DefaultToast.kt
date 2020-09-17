package com.sergstas.lib.toasts

import android.content.Context
import android.widget.Toast

public fun makeDefaultToast(context: Context, content: String) {
    val toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
    toast.show()
}