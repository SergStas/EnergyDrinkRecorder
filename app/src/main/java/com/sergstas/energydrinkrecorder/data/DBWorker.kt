package com.sergstas.energydrinkrecorder.data

import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.ENTRIES_ID
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.POSITIONS_ID
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.extensions.select
import com.sergstas.extensions.toArrayList
import com.sergstas.lib.sql.dbcontrol.DBController
import com.sergstas.lib.sql.models.Row
import java.lang.Exception
import java.sql.Date

@ExperimentalStdlibApi
class DBWorker public constructor(controller: DBController) {
    private val _controller = controller

    fun getStatsByDay(): Pair<Double, Double>? {
        val curDate = Date(System.currentTimeMillis()).toString()
        val rows = _controller.selectBy(ENTRIES_ID, "date", curDate)
            ?: return Pair(0.0, 0.0)
        var liters = 0.0
        var cost = 0.0
        for (row in rows)
            try {
                val count = row.getValue("count") as Int
                val id = row.getValue("edId") as Int
                val edData = _controller.selectBy(POSITIONS_ID, "_id", id)!!.first()
                liters += count * (edData.getValue("volume") as Float)
                cost += count * (edData.getValue("price") as Float)
            }
            catch (e: Exception) {
                return null
            }
        return Pair(liters, cost)
    }

    fun getAllPosInfo(): ArrayList<PositionInfo> {
        return _controller.getAllPositions(POSITIONS_ID)!!.select { row -> PositionInfo(row) }.toArrayList()
    }

    fun getAllEntryInfo(): ArrayList<EntryInfo> {
        return _controller.getAllPositions(ENTRIES_ID)!!.select { row -> entryRowToEntryInfo(row) }.toArrayList()
    }

    fun getEDIdByName(name: String): Int {
        return (_controller.getAllPositions(POSITIONS_ID)!!
            .first { pos -> pos.getValue("name") as String == name }
            .getValue("_id")) as Int
    }

    fun addNewEntry(edId: Int, count: Int, date: String) {
        val row = Row(_controller.getTable(ENTRIES_ID)!!)
        row.fill(arrayListOf(null, edId, count, date))
        _controller.tryAddPosition(ENTRIES_ID, row)
    }

    fun addNewPosition(name: String, volume: Float, price: Float) {
        val row = Row(_controller.getTable(POSITIONS_ID)!!)
        row.fill(arrayListOf(null, name, volume, price))
        _controller.tryAddPosition(POSITIONS_ID, row)
    }

    fun tryRemovePosition(id: Int): Boolean {
        if (_controller.selectBy(ENTRIES_ID, "_id", id)?.count() != 0)
            return _controller.tryRemoveBy(ENTRIES_ID, "_id", id)
        return false
    }

    private fun entryRowToEntryInfo(entry: Row): EntryInfo {
        val edId = entry.getValue("edId") as Int
        val position = _controller.selectBy(POSITIONS_ID, "_id", edId)!!.first()
        return EntryInfo(entry, position)
    }
}