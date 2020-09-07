package com.sergstas.energydrinkrecorder.models

import android.os.Parcel
import android.os.Parcelable
import com.sergstas.lib.sql.models.Row
import kotlin.reflect.cast

@ExperimentalStdlibApi
class PositionInfo constructor(val id: Int): Parcelable {
    var name: String? = null
    var volume: Float? = null
    var price: Float? = null

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        name = parcel.readString()
        volume = parcel.readValue(Float::class.java.classLoader) as? Float
        price = parcel.readValue(Float::class.java.classLoader) as? Float
    }

    constructor(row: Row): this(Int::class.cast(row.getValue("_id"))) {
        fillFromRow(row)
    }

    private fun fillFromRow(row: Row) {
        name = String::class.cast(row.getValue("name"))
        volume = Float::class.cast(row.getValue("volume"))
        price = Float::class.cast(row.getValue("price"))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeValue(volume)
        parcel.writeValue(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PositionInfo> {
        override fun createFromParcel(parcel: Parcel): PositionInfo {
            return PositionInfo(parcel)
        }

        override fun newArray(size: Int): Array<PositionInfo?> {
            return arrayOfNulls(size)
        }
    }
}