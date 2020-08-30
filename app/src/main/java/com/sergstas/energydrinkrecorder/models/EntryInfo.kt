package com.sergstas.energydrinkrecorder.models

import com.sergstas.lib.sql.models.Row
import java.lang.Exception
import kotlin.reflect.cast

@ExperimentalStdlibApi
class EntryInfo constructor(var entryId: Int, var edId: Int) {
    var edName: String? = null
    var volume: Float? = null
    var price: Float? = null
    var count: Int? = null
    var date: String? = null

    val isFilled: Boolean
        get() { return edName != null && volume != null && price != null && count != null && date != null }

    fun fillFromEntriesRow(row: Row): Boolean {
        return try {
            if (row.getValue("_id") != entryId)
                return false
            count = Int::class.cast(row.getValue("count"))
            date = String::class.cast(row.getValue("date"))
            true
        }
        catch (e: Exception) {
            false
        }
    }

    fun fillFromPositionsRow(row: Row): Boolean {
        return try {
            if (row.getValue("_id") != edId)
                return false
            volume = Float::class.cast(row.getValue("volume"))
            price = Float::class.cast(row.getValue("price"))
            edName = String::class.cast(row.getValue("name"))
            true
        }
        catch (e: Exception) {
            false
        }
    }
}