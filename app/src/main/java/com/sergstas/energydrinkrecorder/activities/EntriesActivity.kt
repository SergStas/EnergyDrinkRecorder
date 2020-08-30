package com.sergstas.energydrinkrecorder.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.lib.sql.dbcontrol.DBController

class EntriesActivity: AppCompatActivity() {
    private val _controller = DBController(this)
    private val _worker: DBWorker

    init {
        _worker = DBWorker(_controller)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
    }
}