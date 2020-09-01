package com.example.desafioeventos.models

/**
 * Created by Marcelo López on 01/09/2020
 */

class Checkin {
    var eventID: String? = null
    var name: String? = null
    var email: String? = null

    constructor() {}

    constructor(eventId: String, name: String, email: String) {
        this.eventID = eventId
        this.name = name
        this.email = email
    }


}
