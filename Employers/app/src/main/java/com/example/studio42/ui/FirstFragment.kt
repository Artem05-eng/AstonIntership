package com.example.studio42.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.studio42.R
import com.example.studio42.databinding.FirstScreenBinding
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.presentation.StartViewModel
import com.example.studio42.util.Listener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FirstFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding: FirstScreenBinding? = null
    private lateinit var viewModel: StartViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FirstScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[StartViewModel::class.java]
        viewModel.getEmploerType()
        viewModel.error.observe(viewLifecycleOwner, {
            binding?.logo?.alpha = 0.2f
            binding?.retryButton?.isVisible = true
            binding?.progress?.isGone = true
            binding?.description?.text = "Похоже проблемы \nс сетью"
        })
        binding?.retryButton?.setOnClickListener {
            binding?.logo?.alpha = 1f
            binding?.progress?.isGone = false
            binding?.description?.text = "Работодатели \n3000"
            it.isVisible = false
            viewModel.getEmploerType()
        }
        viewModel.data.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                findNavController().navigate(
                    FirstFragmentDirections.actionFirstFragmentToSecondFragment(RequestEmployer("", "", false))
                )
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}