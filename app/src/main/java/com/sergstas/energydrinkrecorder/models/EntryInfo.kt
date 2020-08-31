package com.sergstas.energydrinkrecorder.models

import android.os.Parcel
import android.os.Parcelable
import com.sergstas.lib.sql.models.Row
import java.lang.Exception
import kotlin.reflect.cast

@ExperimentalStdlibApi
class EntryInfo constructor(var entryId: Int, var edId: Int): Parcelable {
    var edName: String? = null
    var volume: Float? = null
    var price: Float? = null
    var count: Int? = null
    var date: String? = null

    val isFilled: Boolean
        get() { return edName != null && volume != null && price != null && count != null && date != null }

    constructor(parcel: Parcel) : this(parcel.readInt(),parcel.readInt()) {
        edName = parcel.readString()
        volume = parcel.readValue(Float::class.java.classLoader) as? Float
        price = parcel.readValue(Float::class.java.classLoader) as? Float
        count = parcel.readValue(Int::class.java.classLoader) as? Int
        date = parcel.readString()
    }

    constructor(id: Int, edId: Int, edName: String, volume: Float, price: Float, count: Int, date: String) : this(id, edId) {
        this.edName = edName
        this.volume = volume
        this.price = price
        this.count = count
        this.date = date
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(entryId)
        parcel.writeInt(edId)
        parcel.writeString(edName)
        parcel.writeValue(volume)
        parcel.writeValue(price)
        parcel.writeValue(count)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntryInfo> {
        override fun createFromParcel(parcel: Parcel): EntryInfo {
            return EntryInfo(parcel)
        }

        override fun newArray(size: Int): Array<EntryInfo?> {
            return arrayOfNulls(size)
        }
    }
}