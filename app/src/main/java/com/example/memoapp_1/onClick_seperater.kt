package com.example.memoapp_1

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

fun onClick_seperater(id:Int?,context: Context){
    when(id){
        R.id.btn_plus -> {
            Toast.makeText(context,"is btn_plus", Toast.LENGTH_SHORT).show()
        }
        else -> {
            Toast.makeText(context,"${id.toString()} is pressed", Toast.LENGTH_SHORT).show()
        }


    }
}