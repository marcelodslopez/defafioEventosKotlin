package com.example.desafioeventos.api

import com.example.desafioeventos.models.Checkin
import com.example.desafioeventos.models.Event
import com.example.desafioeventos.models.EventDetail

import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST



interface EventAPI {

    @GET("events")
    fun getEvents(): Call<List<Event>>

    @GET("events/{id}")
    fun getEventDetail(@Path("id") id: String): Call<EventDetail>

    @FormUrlEncoded
    @POST("checkin")
    fun sendChekin(@Field("eventId") eventId:String,
                   @Field("name") name:String,
                   @Field("email") email:String
    ): Call<Checkin>

    companion object {
        val BASE_URL = "http://5b840ba5db24a100142dcd8c.mockapi.io/api/"
    }

}
