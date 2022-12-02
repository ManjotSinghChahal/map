package com.app.map.data.local

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class AppEntities() : Parcelable, Comparable<AppEntities> {
    @PrimaryKey
    @NonNull
    var id : String = ""
    var latitude: Double? = null
    var longitude: Double? = null
    var city: String? = null
    var address: String? = null
    var distance: Double? = null
    var primary: Int? = null

    override fun compareTo(other: AppEntities): Int = when {
        distance != other.distance -> distance!!.toInt() - other.distance!!.toInt()
        else -> distance!!.toInt()
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        primary = parcel.readValue(Int::class.java.classLoader) as? Int
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double
        city = parcel.readString()
        address = parcel.readString()
        distance = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeValue(primary)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(city)
        parcel.writeString(address)
        parcel.writeValue(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppEntities> {
        override fun createFromParcel(parcel: Parcel): AppEntities {
            return AppEntities(parcel)
        }

        override fun newArray(size: Int): Array<AppEntities?> {
            return arrayOfNulls(size)
        }
    }

}
