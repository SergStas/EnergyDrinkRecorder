package com.sergstas.energydrinkrecorder.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.sergstas.energydrinkrecorder.R

class EditEntryActivity: AppCompatActivity() {
    private var _id: Int? = null
    private var _count: Int? = null
    private var _date: String? = null
    private var _edName: String? = null

    //TODO: finish
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_new_entry)
        
    }
}