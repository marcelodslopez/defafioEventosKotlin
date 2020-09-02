package com.example.desafioeventos.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Marcelo López on 01/09/2020
 */


class Cupom() : Parcelable {
    lateinit var id: String
    lateinit var eventId: String
    var discount: Long = 0


    constructor(parcel: Parcel) : this() {
        id = parcel.readString()!!
        eventId = parcel.readString()!!
        discount = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(eventId)
        parcel.writeLong(discount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cupom> {
        override fun createFromParcel(parcel: Parcel): Cupom {
            return Cupom(parcel)
        }

        override fun newArray(size: Int): Array<Cupom?> {
            return arrayOfNulls(size)
        }
    }

}
