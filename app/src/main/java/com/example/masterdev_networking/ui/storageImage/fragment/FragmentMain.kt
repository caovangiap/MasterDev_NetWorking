package com.example.masterdev_networking.ui.storageImage.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.masterdev_networking.R
import com.example.masterdev_networking.databinding.FragmentStoreImageBinding
import com.example.masterdev_networking.storage.StorageLogin
import com.example.masterdev_networking.ui.MainActivity
import com.example.masterdev_networking.ui.storageImage.adapter.AdapterImageStorage
import com.example.masterdev_networking.viewModel.ViewModelStoreImage
import kotlinx.coroutines.*

class FragmentMain : Fragment() {
    lateinit var binding: FragmentStoreImageBinding
    lateinit var viewModel: ViewModelStoreImage


    var mView: View? = null
    val REQUES_PERMISSION = 100

    var adapterImage: AdapterImageStorage?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreImageBinding.inflate(inflater, container, false)
        mView = binding.root
        viewModel = MainActivity.viewModelStorage
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setUpUi()
        return mView
    }

    fun setUpUi() {
        // hiển thị avatar lấy ảnh từ storage ra
        context?.let { it1 ->
            Glide.with(it1)
                .load(StorageLogin.getString(StorageLogin.IMAGE))
                .apply(
                    RequestOptions.bitmapTransform(CircleCrop())
                )
                .placeholder(R.drawable.backgroud_avatar)
                .into(binding.Avatar)
        }
        binding.fullName.text = StorageLogin.getString(StorageLogin.FULL_NAME)
        checkPermission()
        binding.CheckPermission.setOnClickListener {
            checkPermission()
        }
    }

    // permission truy cập vào storage media
    fun checkPermission() {

        // biến lắng nghe sự kiện nếu người dùng chọn don't ask again
        val checkDontAskAgain =
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)

        // check permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Android 11 or higher
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

                // nếu người dùng chọn don't ask again
                binding.Check.setOnClickListener {
                    if (!checkDontAskAgain) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context?.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
                Environment.isExternalStorageManager()
            }
            // Android below 11
            else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUES_PERMISSION
                )
                // nếu người dùng chọn don't ask again
                binding.Check.setOnClickListener {
                    if (!checkDontAskAgain) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context?.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            }
        } else {
            binding.Check.visibility = View.GONE
            loadImage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView = null
    }


    // hiển thị ảnh
    @OptIn(DelicateCoroutinesApi::class)
    fun loadImage() {
        viewModel.getImageList().observe(viewLifecycleOwner) {
            adapterImage = AdapterImageStorage(it)
            Log.d("data", it.toString())
        }
        // load images
        viewModel.getAllImages()
        binding.listImage.visibility = View.VISIBLE

        // delay chờ observer data
        GlobalScope.launch(Dispatchers.Main) {
            val delay = GlobalScope.launch {
                delay(1000)
            }
            delay.join()
            if (adapterImage!= null){
                binding.listImage.adapter = adapterImage
                binding.listImage.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    // kêt quá phản hồi sau khi xin quyền từ người dùng
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUES_PERMISSION) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

                if (Environment.isExternalStorageManager()) {
                    loadImage()
                    binding.Check.visibility = View.GONE
                }
            } else {
                if (permissions.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context, "Access Permission", Toast.LENGTH_SHORT).show()
                    loadImage()
                    binding.Check.visibility = View.GONE
                }
            }
        }
    }
}
