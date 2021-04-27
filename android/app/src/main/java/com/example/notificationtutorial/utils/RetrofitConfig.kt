package com.example.notificationtutorial.utils

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitConfig {
    companion object {
        var gson = GsonBuilder()
            .setLenient()
            .create()
        fun getRetrofitInstance(path: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}