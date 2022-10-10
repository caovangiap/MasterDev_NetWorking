package com.example.masterdev_networking.networking.apiLogin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiLogin {
    private var retrofit: Retrofit? = null
    val apiService: ApiService
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://admin.giaohangtietkiem.vn")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
}
