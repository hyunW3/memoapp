package com.example.memoapp_1

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") q: String?, @Query("appid") appid: String?): Call<Any?>?

    companion object {
        const val API_URL = "https://api.openweathermap.org/"
    }
}

