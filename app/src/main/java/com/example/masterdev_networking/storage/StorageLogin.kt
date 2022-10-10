package com.example.masterdev_networking.storage

import android.content.Context
import com.example.masterdev_networking.ui.MainActivity

object StorageLogin {
    const val SHARED_PREFERENCES_NAME = "Check Login"
    const val ACCESS = "Check Access"
    const val UserName = "UserName"
    const val PASSWORD = "PassWord"
    const val IMAGE = "Image_ User"
    const val FULL_NAME = "full name user"

    private  val Share = MainActivity.ApplicationContext.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    private val edit = Share.edit()

    fun getString(key: String,ValueDefault: String= "False"): String?{
        return Share.getString(key,ValueDefault)
    }
    fun putKey(Key:String,Value: String) {
        return edit.putString(Key,Value).apply()
    }

    fun remove(Key:String){
        edit.remove(Key)
        Share.edit().clear().apply()
    }
}