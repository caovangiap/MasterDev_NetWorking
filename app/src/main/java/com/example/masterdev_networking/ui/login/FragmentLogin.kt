package com.example.masterdev_networking.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.masterdev_networking.R
import com.example.masterdev_networking.databinding.FragmentLoginBinding
import com.example.masterdev_networking.ui.MainActivity
import com.example.masterdev_networking.viewModel.ViewModelLogin

class FragmentLogin : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel : ViewModelLogin
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = MainActivity.viewModel
        binding.login = viewModel
        binding.lifecycleOwner = this
        SetUpToolBar()
        return view
    }

    fun SetUpToolBar(){
        binding.ToolBar.setNavigationIcon(R.drawable.back)
        //yêu cầu activity điều hướng quay trở lại
        binding.ToolBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.color.observe(viewLifecycleOwner){
            when(it){
                "green"->{
                    binding.Login.setBackgroundResource(R.color.GreenButton)
                }
                "gray"->{
                    binding.Login.setBackgroundResource(R.color.GrayButton)
                }
            }
        }
    }
}