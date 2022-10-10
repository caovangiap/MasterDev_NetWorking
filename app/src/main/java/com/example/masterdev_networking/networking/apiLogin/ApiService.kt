package com.example.masterdev_networking.networking.apiLogin


import com.example.masterdev_networking.model.modelLogin.User
import com.example.masterdev_networking.model.modelLogin.DataResponeLogin
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers(
        "appVersion:4000",
        "isMobileApp:1"
    )
    @FormUrlEncoded
    @POST("/admin/login")
    fun posts(
        @Field("User[username]") name: String,
        @Field("User[password]") pass: String
    ): Call<User>

    @GET("/admin/AdProfiles/getUserEkycInfo")
    fun getDataLogin(@Header("Cookie") token: String): Call<DataResponeLogin>
}