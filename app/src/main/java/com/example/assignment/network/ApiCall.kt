package com.example.assignment.network

import com.example.assignment.datamodels.Item
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiCall {
    @get:GET("v3/f79cbce1-a70e-4313-8d76-00d19ee3b4c1")
    val item: Call<List<Item>>

    companion object{
        operator fun invoke(): ApiCall {
            return Retrofit.Builder()
                .baseUrl("https://run.mocky.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiCall::class.java)
        }
    }
}