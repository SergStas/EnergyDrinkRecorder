package com.sergstas.energydrinkrecorder.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R
import kotlinx.android.synthetic.main.dialog_assertion_window.*
import java.lang.Exception

class DialogActivity: AppCompatActivity() {
    companion object {
        const val REMOVE_ALL_REQUEST = 0
        const val REMOVE_ID_REQUEST = 1

        const val TEXT_ARG_KEY = "text"
        const val ACCEPT_ARG_KEY = "acceptStr"
        const val DECLINE_ARG_KEY = "declineStr"
        const val REQUEST_RESULT_KEY = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_assertion_window)
        setup()
        setListeners()
    }

    private fun setListeners() {
        assert_bAccept.setOnClickListener {
            finishActivity(true)
        }
        assert_bDecline.setOnClickListener {
            finishActivity(false)
        }
    }

    private fun finishActivity(result: Boolean) {
        val finishIntent = Intent()
        finishIntent.putExtra(REQUEST_RESULT_KEY, result)
        setResult(Activity.RESULT_OK, finishIntent)
        finish()
    }

    private fun setup() {
        try {
            assert_text.text = intent.getStringExtra(TEXT_ARG_KEY)
            assert_bAccept.text = intent.getStringExtra(ACCEPT_ARG_KEY)
            assert_bDecline.text = intent.getStringExtra(DECLINE_ARG_KEY)
        }
        catch (e: Exception) {
            throw IllegalArgumentException("No args for $this")
        }
    }
}