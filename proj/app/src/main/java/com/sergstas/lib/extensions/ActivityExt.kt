package com.sergstas.lib.extensions

import android.app.Activity
import com.sergstas.lib.toasts.makeDefaultToast
import java.lang.Exception

public fun Activity.handleCrash(op: () -> Unit) {
    try {
        op()
    }
    catch (e: Exception) {
        makeDefaultToast(this, "Unhandled exception in $this: ${e.message}")
        this.finish()
    }
}