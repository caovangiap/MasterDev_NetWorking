package com.example.masterdev_networking.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.masterdev_networking.R
import com.example.masterdev_networking.storage.StorageLogin
import com.example.masterdev_networking.ui.login.FragmentLayout
import com.example.masterdev_networking.ui.login.FragmentLogin
import com.example.masterdev_networking.ui.profile.FragmentProfile
import com.example.masterdev_networking.ui.storageImage.fragment.FragmentMain
import com.example.masterdev_networking.ui.takephoto.FragmentTakePhoto
import com.example.masterdev_networking.viewModel.ViewModelLogin
import com.example.masterdev_networking.viewModel.ViewModelStoreImage

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var ApplicationContext: Context
        lateinit var viewModel: ViewModelLogin
        lateinit var viewModelStorage : ViewModelStoreImage
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApplicationContext = this
        viewModel = ViewModelLogin()
        viewModelStorage = ViewModelStoreImage(this)
        checkSaveUser()
        replaceFragment()
    }

    // check save pass
    fun checkSaveUser() {
        val UserName = StorageLogin.getString(StorageLogin.UserName, "name")
        val PassWord = StorageLogin.getString(StorageLogin.PASSWORD, "pass")

        // call api check Pass đã thay đổi hay chưa
        // đồng thời call api data lại mỗi lần chạy app
        viewModel.checkSavePass(UserName!!, PassWord!!)

        if (StorageLogin.getString(StorageLogin.ACCESS, "False") == "True") {
            val Manager = supportFragmentManager.beginTransaction()
            val fragment = FragmentMain()
            Manager.add(R.id.container, fragment)
            Manager.commit()
            StorageLogin.getString(StorageLogin.ACCESS)?.let { Log.d("storage", it) }
        } else {
            val Manager = supportFragmentManager.beginTransaction()
            val fragment = FragmentLayout()
            Manager.add(R.id.container, fragment)
            Manager.commit()
        }
    }

    // điều hướng các màn
    fun replaceFragment() {
        viewModel.changeFragment.observe(this) {
            val manager = supportFragmentManager.beginTransaction()
            when (it) {
                "Login" -> {
                    val fragment = FragmentLogin()
                    manager.replace(R.id.container, fragment)
                    manager.addToBackStack(null)
                    manager.commit()
                }
                "Access" ->{
                    val fragment = FragmentMain()
                    manager.replace(R.id.container, fragment)
                    manager.addToBackStack(null)
                    manager.commit()
                }

            }
        }
        viewModelStorage.changFragment.observe(this){
            val manager = supportFragmentManager.beginTransaction()
            when(it){
                "Login"->{
                    val fragment = FragmentLogin()
                    manager.replace(R.id.container, fragment)
                    manager.addToBackStack(null)
                    manager.commit()
                }
                "profile"->{
                    val fragment = FragmentProfile()
                    manager.replace(R.id.container, fragment)
                    manager.addToBackStack(null)
                    manager.commit()
                }
                "Image" ->{
                    val fragment = FragmentTakePhoto()
                    manager.replace(R.id.container, fragment)
                    manager.addToBackStack(null)
                    manager.commit()
                }
                "Main" ->{
                    val fragment = FragmentMain()
                    manager.replace(R.id.container, fragment)
                    manager.addToBackStack(null)
                    manager.commit()
                }
            }
        }
    }


}