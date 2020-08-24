package com.sergstas.energydrinkrecorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergstas.energydrinkrecorder.data.OpenHelper
import com.sergstas.extensions.select

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}