package com.example.lostandfound3.activities.models

import android.os.Parcel
import android.os.Parcelable

data class found (
    var item_image2:String="",
    val item_name2:String ="",
    val foundwhere:String="",
    val claim1:String=""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(item_image2)
        parcel.writeString(item_name2)
        parcel.writeString(foundwhere)
        parcel.writeString(claim1)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<found> {
        override fun createFromParcel(parcel: Parcel):found {
            return found(parcel)
        }

        override fun newArray(size: Int): Array<found?> {
            return arrayOfNulls(size)
        }
    }}


