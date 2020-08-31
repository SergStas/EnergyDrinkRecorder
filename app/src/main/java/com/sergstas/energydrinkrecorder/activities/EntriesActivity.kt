package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.sergstas.energydrinkrecorder.R
import com.sergstas.energydrinkrecorder.data.DBWorker
import com.sergstas.energydrinkrecorder.fragments.EntriesListFragment
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.lib.sql.dbcontrol.DBController
import java.util.*

@ExperimentalStdlibApi
class EntriesActivity: AppCompatActivity() {
    private val _controller = DBController(this)
    private val _worker: DBWorker

    init {
        _worker = DBWorker(_controller)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        val bundle = Bundle()
        bundle.putParcelableArrayList("rows", arrayListOf(
            EntryInfo(1, 3, "eon", 0.5f, 45.67f, 2, Date(System.currentTimeMillis()).toString()),
            EntryInfo(2, 2, "monster", 0.45f, 66f, 1, Date(System.currentTimeMillis()).toString())
        ))
        val fragment = EntriesListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.activity_entries_root, fragment, "").commit()
    }
}