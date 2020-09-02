package com.example.desafioeventos.model

import android.os.Parcel

/**
 * Created by Marcelo López on 01/09/2020
 */

class Event {

    var id: String
    var title: String
    var image: String? = null

    constructor(parcel: Parcel)  {
        id = parcel.readString().toString()
        title = parcel.readString().toString()
        image = parcel.readString()

    }


}



