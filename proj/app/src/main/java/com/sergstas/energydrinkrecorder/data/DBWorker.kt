package com.sergstas.energydrinkrecorder.data

import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.ENTRIES_ID
import com.sergstas.energydrinkrecorder.data.DBHolderActivity.TablesId.Companion.POSITIONS_ID
import com.sergstas.energydrinkrecorder.models.EntryInfo
import com.sergstas.energydrinkrecorder.models.PositionInfo
import com.sergstas.lib.extensions.select
import com.sergstas.lib.extensions.toArrayList
import com.sergstas.lib.sql.dbcontrol.DBController
import com.sergstas.lib.sql.models.Row
import java.sql.Date
import java.util.zip.DataFormatException

@ExperimentalStdlibApi
class DBWorker public constructor(controller: DBController) {
    private val _controller = controller

    fun getStatsByDay(): Pair<Double, Double>? {
        val curDate = Date(System.currentTimeMillis()).toString()
        return countVolumeAndPriceFrom(_controller.selectBy(ENTRIES_ID, "date", curDate))
    }

    fun getAllPosInfo(): ArrayList<PositionInfo> {
        return _controller.getAllPositions(POSITIONS_ID).select { row -> PositionInfo(row) }.toArrayList()
    }

    fun getAllEntryInfo(): ArrayList<EntryInfo> {
        return _controller.getAllPositions(ENTRIES_ID).select { row -> entryRowToEntryInfo(row) }.toArrayList()
    }

    fun getTotalStats(): Pair<Double, Double>? {
        return countVolumeAndPriceFrom(_controller.getAllPositions(ENTRIES_ID))
    }

    fun addNewEntry(edId: Int, count: Int, date: String): Boolean {
        val row = Row(_controller.getTable(ENTRIES_ID)!!)
        row.fill(arrayListOf(null, edId, count, date))
        return _controller.addPosition(ENTRIES_ID, row)
    }

    fun addNewPosition(name: String, volume: Float, price: Float): Boolean {
        val row = Row(_controller.getTable(POSITIONS_ID)!!)
        row.fill(arrayListOf(null, name, volume, price))
        return _controller.addPosition(POSITIONS_ID, row)
    }

    fun removePosition(id: Int): Boolean {
        if (_controller.selectBy(POSITIONS_ID, "_id", id)?.count() != 0)
            return _controller.removeBy(POSITIONS_ID, "_id", id)
        return false
    }

    fun removeAllPositions(): Boolean {
        return _controller.clear(POSITIONS_ID)
    }

    fun removeEntry(id: Int): Boolean {
        if (_controller.selectBy(ENTRIES_ID, "_id", id)?.count() != 0)
            return _controller.removeBy(ENTRIES_ID, "_id", id)
        return false
    }

    fun removeAllEntries(): Boolean {
        return _controller.clear(ENTRIES_ID)
    }

    fun removeRelatedEntries(id: Int): Boolean {
        if (_controller.selectBy(ENTRIES_ID, "edId", id) == null)
            return false
        val ids = _controller.selectBy(ENTRIES_ID, "edId", id)!!.select { row -> row.getValue("_id") as Int }
        for (curId in ids)
            _controller.removeBy(ENTRIES_ID, "_id", curId)
        return true
    }

    fun getPosInfoById(id: Int): PositionInfo? {
        return if(_controller.selectBy(POSITIONS_ID, "_id", id)?.first() == null) null
        else posRowToPosInfo(_controller.selectBy(POSITIONS_ID, "_id", id)?.first()!!)
    }

    fun updateEntry(oldEntry: EntryInfo, newEntry: EntryInfo): Boolean {
        if (oldEntry.edId != newEntry.edId && !_controller.updateSinglePosition(
            ENTRIES_ID, "edId", newEntry.edId, "_id", oldEntry.entryId))
            return false
        if (oldEntry.date != newEntry.date && !_controller.updateSinglePosition(
                ENTRIES_ID, "date", newEntry.date, "_id", oldEntry.entryId))
            return false
        if (oldEntry.count != newEntry.count && !_controller.updateSinglePosition(
                ENTRIES_ID, "count", newEntry.count, "_id", oldEntry.entryId))
            return false
        return true
    }

    private fun entryRowToEntryInfo(entry: Row): EntryInfo {
        if (!entry.isFilled)
            throw DataFormatException("Unable to create EntryInfo object from $entry: row wasn't filled")
        val edId = entry.getValue("edId") as Int
        val position = _controller.selectBy(POSITIONS_ID, "_id", edId)!!.first()
        return EntryInfo(entry, position)
    }

    private fun posRowToPosInfo(row: Row): PositionInfo {
        if (!row.isFilled)
            throw DataFormatException("Unable to create EntryInfo object from $row: row wasn't filled")
        return PositionInfo(row)
    }

    private fun countVolumeAndPriceFrom(rows: Iterable<Row>?): Pair<Double, Double>? {
        if (rows == null)
            return Pair(0.0, 0.0)
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
}