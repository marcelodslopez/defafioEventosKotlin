package com.example.desafioeventos.adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.desafioeventos.view.DetailEventActivity
import com.example.desafioeventos.R
import com.example.desafioeventos.api.EventAPI
import com.example.desafioeventos.model.Event
import com.example.desafioeventos.model.EventDetail
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Created by Marcelo López on 01/09/2020
 */

class EventAdapter(var activity: Activity, var context: Context, private var list: List<Event>?) : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    private val layoutInflater: LayoutInflater
    lateinit var dialog: ProgressDialog


    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.item_card, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.iv.setImageResource(R.drawable.ic_menu_camera);
        holder.tv_name.text = list!![position].title
        dialog = ProgressDialog(activity)
        dialog.setMessage("Buscando detalhes, aguarde...")
        Picasso.with(context)
                .load(list!![position].image)
                .placeholder(context.resources.getDrawable(R.drawable.event))
                .error(context.resources.getDrawable(R.drawable.event))
                .into(holder.iv)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun setList(eventsList: List<Event>) {
        this.list = eventsList
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var iv: ImageView
        var tv_name: TextView

        init {
            iv = itemView.findViewById<View>(R.id.iv_picture) as ImageView
            tv_name = itemView.findViewById<View>(R.id.tv_name) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            val event = list!![position]
            val intent = Intent(view.context, DetailEventActivity::class.java)


            dialog.show()

            val retrofit = Retrofit.Builder()
                    .baseUrl(EventAPI.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

            //eventApi = retrofit.create(EventAPI::class.java)
            val call  = retrofit.create(EventAPI::class.java).getEventDetail(event.id)
            call.enqueue(object : Callback<EventDetail> {
                override fun onFailure(call: Call<EventDetail>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    dialog.dismiss()
                }

                override fun onResponse(call: Call<EventDetail>?, response: Response<EventDetail>?) {
                    var eventDetail = response!!.body()
                    intent.putExtra("event", eventDetail as Parcelable)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    view.context.startActivity(intent)
                    dialog.dismiss()
                }

            })
        }
    }

}
