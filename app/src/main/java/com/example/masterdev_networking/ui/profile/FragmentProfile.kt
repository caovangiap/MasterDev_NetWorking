package com.example.masterdev_networking.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.masterdev_networking.R
import com.example.masterdev_networking.databinding.FragmentProfileBinding
import com.example.masterdev_networking.storage.StorageLogin
import com.example.masterdev_networking.ui.MainActivity
import com.example.masterdev_networking.viewModel.ViewModelLogin

class FragmentProfile : Fragment() {
    lateinit var binding : FragmentProfileBinding
    lateinit var viewModel : ViewModelLogin
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = MainActivity.viewModel
        binding.viweModel = viewModel
        binding.lifecycleOwner = this
        SetUpToolBar()
        setUi()
        return view
    }

    fun SetUpToolBar(){
        binding.ToolBar.setNavigationIcon(R.drawable.back)
        //yêu cầu activity điều hướng quay trở lại
        binding.ToolBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    fun setUi(){
        context?.let { it1 ->
            Glide.with(it1)
                .load(StorageLogin.getString(StorageLogin.IMAGE))
                .apply(
                    RequestOptions.bitmapTransform(CircleCrop())
                )
                .placeholder(R.drawable.backgroud_avatar)
                .into(binding.Avatar)
        }
        binding.UserName.text = StorageLogin.getString(StorageLogin.FULL_NAME)
        viewModel.dataApi.tel.observe(viewLifecycleOwner){
            binding.UserPhone.text = "Tel : $it"
        }
        viewModel.dataApi.current_district.observe(viewLifecycleOwner){
            binding.UserSchool.text = "Trình độ đại học: $it"
        }
        viewModel.dataApi.current_address_detail.observe(viewLifecycleOwner){
            binding.UserCurentAddress.text = "Địa chỉ : $it"
        }
        viewModel.dataApi.nationality.observe(viewLifecycleOwner){
            binding.UserOld.text = "Quận: $it"
        }

        viewModel.dataApi.personal_email.observe(viewLifecycleOwner){
            binding.allInfomation.text
        }

        Log.d("data",viewModel.dataApi.toString())
    }

}