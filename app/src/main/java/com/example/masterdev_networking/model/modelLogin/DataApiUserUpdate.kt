package com.example.masterdev_networking.model.modelLogin

import androidx.lifecycle.MutableLiveData

object DataApiUserUpdate {
    var nameUser = MutableLiveData<String>()
    var imageUser = MutableLiveData<String>()
    // địa chỉ hiện tại
    var current_address_detail= MutableLiveData<String>()
    // số nhà
    var current_address_district = MutableLiveData<String>()
    // quận
    var current_district = MutableLiveData<String>()
    // tỉnh
    var current_province = MutableLiveData<String>()
    // phường
    var current_sub_district = MutableLiveData<String>()
    // học vấn
    var degree = MutableLiveData<String>()
    // dân tôc
    var ethnic = MutableLiveData<String>()
    // giới tính
    var marital_status= MutableLiveData<String>()
    // quốc tịch
    var nationality= MutableLiveData<String>()
    // quê quán thành phố
    var native_district = MutableLiveData<String>()
    // quê quán tỉnh
    var native_province = MutableLiveData<String>()
    // quê quán xã
    var native_sub_district = MutableLiveData<String>()
    // gmail
    var personal_email = MutableLiveData<String>()
    // đại chỉ nhà
    var address_detail = MutableLiveData<String>()
    // số điện thoại
    var tel = MutableLiveData<String>()
}