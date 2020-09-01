package com.example.desafioeventos.util

import android.content.Context
import android.widget.Toast

/**
 * Created by Marcelo López on 01/09/2020
 */

 class Utilidades{
     companion object {
         fun showToast(message : String, context: Context){
             Toast.makeText(context,
                     message,
                     Toast.LENGTH_LONG).show()
         }
     }

}
