package com.example.memoapp_1

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView
import kotlinx.android.synthetic.main.intro.*

class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)
        val weatherView:WeatherView  = findViewById (R.id.weather_view)
        //weather_view.setWeatherData(PrecipType.RAIN)
        write_img.alpha = 0.3F
        weather_view.setWeatherData(PrecipType.SNOW)
        ToMemo.setOnClickListener {
            onclick(it)
        }
        ToDday.setOnClickListener{
            onclick(it)
        }
    }

    private fun onclick(v: View?) {
        //ToMemo.text = "clicked"
        var i: Intent?= null
        if(v?.id == ToMemo.id){
            i= Intent(this,MainActivity::class.java)
        }else {
            i= Intent(this,d_day::class.java)
        }
        startActivity(i)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}