package com.example.desafioeventos.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.*

/**
 * Created by Marcelo López on 01/09/2020
 */

class EventDetail : Serializable, Parcelable {

    lateinit var id: String
    lateinit var title: String
    var price: Double = 0.0
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var image: String? = null
    lateinit var description: String
    var date: Long = 0
    var people: List<Person>
    var cupons: List<Cupom>


    constructor(parcel: Parcel)  {
        id = parcel.readString().toString()
        title = parcel.readString().toString()
        price = (parcel.readValue(Double::class.java.classLoader) as? Double)!!
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double as Double
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double as Double
        image = parcel.readString()
        description = parcel.readString().toString()
        date = (parcel.readValue(Long::class.java.classLoader) as? Long)!!

        people = parcel.readValue(Person::class.java.getClassLoader()) as List<Person>
        cupons = parcel.readValue(Cupom::class.java.getClassLoader()) as List<Cupom>

    }

    constructor() {
        this.people = ArrayList()
        this.cupons = ArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeValue(price)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeValue(date)
        parcel.writeValue(people)
        parcel.writeValue(cupons)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventDetail> {
        override fun createFromParcel(parcel: Parcel): EventDetail {
            return EventDetail(parcel)
        }

        override fun newArray(size: Int): Array<EventDetail?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(ob: Any?): Boolean {
        if (this === ob) return true
        if (ob == null || javaClass != ob.javaClass) return false

        val post = ob as Event?

        return if (id != null) id == post!!.id else post!!.id == null

    }

    override fun hashCode(): Int {
        return if (id != null) id.hashCode() else 0
    }

}



