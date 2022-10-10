package com.example.masterdev_networking.ui.takephoto

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.masterdev_networking.databinding.FragmentTakePhotoBinding
import com.example.masterdev_networking.ui.MainActivity
import com.example.masterdev_networking.viewModel.ViewModelStoreImage
import java.io.File

class FragmentTakePhoto : Fragment() {

    lateinit var binding: FragmentTakePhotoBinding
    lateinit var viewModel: ViewModelStoreImage
    val CAMERA_REQUEST_CODE = 101
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTakePhotoBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = MainActivity.viewModelStorage
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        capturePhoto()
        binding.TakePhoto.setOnClickListener {
            capturePhoto()
        }
        return view
    }

    // function use camera
    private fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.TakePhoto.visibility = View.VISIBLE
            binding.TakePhoto.setOnClickListener {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        } else {
            binding.textIntro.visibility = View.GONE
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE&& resultCode == RESULT_OK){
            val myBitmap = data?.extras?.get("data")
            viewModel.saveMediaToStorage(myBitmap as Bitmap,requireContext())
            if (myBitmap == ""){
                viewModel.changFragment.postValue("Main")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==CAMERA_REQUEST_CODE){
            if (permissions.isNotEmpty()&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(intent)
            }
        }
    }

}