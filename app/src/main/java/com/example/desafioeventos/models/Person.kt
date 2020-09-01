package com.example.desafioeventos.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by Marcelo López on 01/09/2020
 */

class Person: Parcelable {

    var id: String? = null
    var eventId: String? = null
    var name: String? = null
    var picture: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        eventId = parcel.readString()
        name = parcel.readString()
        picture = parcel.readString()
    }

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}




    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(eventId)
        parcel.writeString(name)
        parcel.writeString(picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val post = o as Event?

        return if (id != null) id == post!!.id else post!!.id == null

    }

    override fun hashCode(): Int {
        return if (id != null) id.hashCode() else 0
    }

}