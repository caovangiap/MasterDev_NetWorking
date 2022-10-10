package com.example.masterdev_networking.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.masterdev_networking.databinding.FragmentLayoutBinding
import com.example.masterdev_networking.ui.MainActivity
import com.example.masterdev_networking.viewModel.ViewModelLogin

class FragmentLayout : Fragment() {
    lateinit var binding : FragmentLayoutBinding
    lateinit var viewModel: ViewModelLogin
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLayoutBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = MainActivity.viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.Login.setOnClickListener {
            viewModel.changeFragment.postValue("Login")
        }
        return view
    }
}