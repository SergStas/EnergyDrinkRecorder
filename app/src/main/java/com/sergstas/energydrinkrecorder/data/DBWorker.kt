package com.sergstas.energydrinkrecorder.data

import com.sergstas.energydrinkrecorder.activities.MainActivity
import com.sergstas.lib.sql.dbcontrol.DBController
import java.lang.Exception
import java.sql.Date
import kotlin.reflect.cast

class DBWorker public constructor(controller: DBController) {
    private val _controller = controller

    @ExperimentalStdlibApi
    fun getStatsByDay(): Pair<Double, Double>? {
        val curDate = Date(System.currentTimeMillis()).toString()
        val rows = _controller.selectBy(MainActivity.ENTRIES_ID, "date", curDate)
            ?: return Pair(0.0, 0.0)
        var liters = 0.0
        var cost = 0.0
        for (row in rows)
            try {
                val count = Int::class.cast(row.getValue("count"))
                val id = Int::class.cast(row.getValue("edId"))
                val edData = _controller.selectBy(MainActivity.POSITIONS_ID, "_id", id)!!.first()
                liters += count * Float::class.cast(edData.getValue("volume"))
                cost += count * Float::class.cast(edData.getValue("price"))
            }
            catch (e: Exception) {
                return null
            }
        return Pair(liters, cost)
    }
}