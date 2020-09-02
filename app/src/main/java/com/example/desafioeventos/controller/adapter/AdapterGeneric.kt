package com.example.desafioeventos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.desafioeventos.R
import com.example.desafioeventos.model.Cupom
import com.example.desafioeventos.model.Person
import kotlinx.android.synthetic.main.item_people.view.*
import kotlin.random.Random


/**
 * Created by Marcelo López on 01/09/2020
 */

class AdapterGeneric(context: Context, private var cupons: List<Cupom>?,private var people: List<Person>?) : BaseAdapter()  {
    private val layoutInflater: LayoutInflater


    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }


    override fun getCount(): Int {
        return if (cupons != null) cupons!!.size else people!!.size
    }

    override fun getItem(position: Int): Any {
        return if (cupons != null) cupons!!.get(position) else people!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return if (cupons != null) cupons!!.get(position).id.toLong() else people!!.get(position).id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if(cupons != null){
            val rowView = layoutInflater.inflate(R.layout.item_people, parent, false)
            var text =  "Desconto de " + cupons!![position].discount.toString() + "%"
            rowView.tv_name_people.text = text
            rowView.iv_picture_people.setImageResource(R.drawable.voucher)
            return rowView
        }else{
            val rowView = layoutInflater.inflate(R.layout.item_people, parent, false)
            rowView.tv_name_people.text = people!![position].name
            rowView.iv_picture_people.setImageResource(getImagePeople())
            return rowView
        }
    }

    private fun getImagePeople(): Int {
        val randomValue = Random.nextInt(1, 3)
        var dw_people : Int = R.drawable.person1
        if (randomValue == 1){
            dw_people =  R.drawable.person1
        }
        if (randomValue == 1){
            dw_people =  R.drawable.person2
        }
        if (randomValue == 1){
            dw_people =  R.drawable.person3
        }
        return dw_people
    }


}
