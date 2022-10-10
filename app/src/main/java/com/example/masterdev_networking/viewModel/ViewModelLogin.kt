package com.example.masterdev_networking.viewModel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.databinding.library.baseAdapters.BR
import com.example.masterdev_networking.storage.StorageLogin
import com.example.masterdev_networking.model.modelLogin.User
import com.example.masterdev_networking.model.modelLogin.DataApiUserUpdate
import com.example.masterdev_networking.model.modelLogin.DataResponeLogin
import com.example.masterdev_networking.networking.apiLogin.ApiLogin
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelLogin : BaseObservable() {
    // Phone Input
    @get:Bindable
    var inputPhone: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inputPhone)
        }

    // input password
    @get:Bindable
    var inputPass: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inputPass)
        }

    // helptext thông báo tình trạng các input được nhập vào
    var requiredUserName = MutableLiveData<String>()
    var requiredPass = MutableLiveData<String>()

    // color button
    var color = MutableLiveData<String>()

    // điều hướng các màn
    var changeFragment = MutableLiveData<String>()

    // Data token
    var mUser: User? = null

    // Data User
    var dataUser: DataResponeLogin? = null

    // data api
    var dataApi = DataApiUserUpdate

    // listent text change
    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (inputPhone != "" && inputPass != "") {
            color.postValue("green")
        } else {
            color.postValue("gray")
        }
    }

    // login
    @OptIn(DelicateCoroutinesApi::class)
    fun logIn() {
        GlobalScope.launch {
            // get token đăng nhập
            val token = GlobalScope.launch {
                ApiLogin.apiService.posts(inputPhone, inputPass).enqueue(object :
                    Callback<User> {
                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        mUser = response?.body()
                        Log.d("token", mUser.toString())
                    }

                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        resetPass()
                    }
                })
                delay(500)
            }
            token.join()
            GlobalScope.launch {
                if (mUser?.token != null) {
                    Log.d("tokencheck", mUser!!.token)
                    ApiLogin.apiService.getDataLogin("PHPSESSID=${mUser!!.token}")
                        .enqueue(object : Callback<DataResponeLogin> {
                            override fun onResponse(
                                call: Call<DataResponeLogin>?,
                                response: Response<DataResponeLogin>?
                            ) {
                                val dataUser = response?.body()
                                if (dataUser?.success.toString() == "true") {
                                    savePass(inputPhone, inputPass, mUser!!.fullname,dataUser!!.data.details.image_profile)
                                    changeFragment.postValue("Access")
                                }
                            }

                            override fun onFailure(call: Call<DataResponeLogin>?, t: Throwable?) {
                                resetPass()
                            }
                        })
                    requiredUserName.postValue("Success")
                    requiredPass.postValue("Success")
                } else {
                    resetPass()
                    requiredUserName.postValue("kiểm tra lại tên đăng nhập")
                    requiredPass.postValue("kiểm tra lại mật khẩu")
                    Log.d("token", mUser.toString())
                }
            }
        }
    }

    fun savePass(inputName: String, inputPass: String, fullName : String, imageProfile: String) {
        StorageLogin.putKey(StorageLogin.UserName, inputName)
        StorageLogin.putKey(StorageLogin.PASSWORD, inputPass)
        StorageLogin.putKey(StorageLogin.ACCESS, "True")
        StorageLogin.putKey(StorageLogin.FULL_NAME,fullName)
        StorageLogin.putKey(StorageLogin.IMAGE,imageProfile)
    }

    // check k có api trả về thì xóa mk và tên đăng nhập lần trước bắt người dùng đăng nhập lại
    fun resetPass() {
        StorageLogin.remove(StorageLogin.UserName)
        StorageLogin.remove(StorageLogin.PASSWORD)
        StorageLogin.remove(StorageLogin.ACCESS)
        changeFragment.postValue("Login")
    }

    // check pass lưu trên máy đã thay đổi hay chưa
    @OptIn(DelicateCoroutinesApi::class)
    fun checkSavePass(Name: String, Pass: String) {
        GlobalScope.launch {
            // get token đăng nhập
            val token = GlobalScope.launch {
                ApiLogin.apiService.posts(Name, Pass).enqueue(object :
                    Callback<User> {
                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        mUser = response?.body()
                    }

                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        resetPass()
                    }
                })
                delay(500)
            }
            token.join()
            if (mUser?.token != null) {
                Log.d("tokencheck", mUser!!.token)
                ApiLogin.apiService.getDataLogin("PHPSESSID=${mUser!!.token}")
                    .enqueue(object : Callback<DataResponeLogin> {
                        override fun onResponse(
                            call: Call<DataResponeLogin>?,
                            response: Response<DataResponeLogin>?
                        ) {
                            dataUser = response?.body()
                            updateDataUi()
                        }

                        override fun onFailure(call: Call<DataResponeLogin>?, t: Throwable?) {
                            resetPass()
                        }
                    })
            }
        }
    }
    fun updateDataUi(){
        dataApi.nameUser.postValue(mUser!!.fullname)
        dataApi.address_detail.postValue(dataUser!!.data.details.current_address_detail)
        dataApi.current_address_detail.postValue(dataUser!!.data.details.current_address_detail)
        dataApi.current_address_district.postValue(dataUser!!.data.details.current_address_district)
        dataApi.current_district.postValue(dataUser!!.data.details.current_district)
        dataApi.current_province.postValue(dataUser!!.data.details.current_province)
        dataApi.current_sub_district.postValue(dataUser!!.data.details.current_sub_district)
        dataApi.degree.postValue(dataUser!!.data.details.degree)
        dataApi.ethnic.postValue(dataUser!!.data.details.ethnic)
        dataApi.marital_status.postValue(dataUser!!.data.details.marital_status)
        dataApi.nationality.postValue(dataUser!!.data.details.nationality)
        dataApi.native_district.postValue(dataUser!!.data.details.native_district)
        dataApi.native_province.postValue(dataUser!!.data.details.native_province)
        dataApi.native_sub_district.postValue(dataUser!!.data.details.native_country_sub_district)
        dataApi.personal_email.postValue(dataUser!!.data.details.personal_email)
        dataApi.tel.postValue(dataUser!!.data.details.tel)
        dataApi.imageUser.value =dataUser?.data?.details?.image_profile
        Log.d("data", dataApi.imageUser.value.toString())
    }


}