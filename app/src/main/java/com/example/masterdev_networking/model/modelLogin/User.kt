package com.example.masterdev_networking.model.modelLogin

data class User(

    val success: Int,
    val message: String,
    val token: String,
    val id: String,
    val status_id: String,
    val fullname: String,
    val username: String

)
