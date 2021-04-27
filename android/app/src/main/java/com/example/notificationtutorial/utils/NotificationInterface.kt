package com.example.notificationtutorial.utils

import com.example.notificationtutorial.models.TokenModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationInterface {
    @POST("token")
    fun registerToken(@Body token : TokenModel) : Call<JsonElement>
}
