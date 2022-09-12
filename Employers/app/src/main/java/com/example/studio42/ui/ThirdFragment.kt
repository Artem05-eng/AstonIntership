package com.example.studio42.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studio42.R
import com.example.studio42.databinding.ThirdScreenBinding
import com.example.studio42.domain.entity.EmloyerType
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.presentation.ThirdViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ThirdFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ThirdViewModel
    private var binding: ThirdScreenBinding? = null
    private val args: ThirdFragmentArgs by navArgs()
    private var data = RequestEmployer("", "", false)
    private var cacheData = RequestEmployer("", "", false)
    private var emplList: List<EmloyerType> = emptyList()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ThirdScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ThirdViewModel::class.java]
        viewModel.getEmployerTypes()
        viewModel.cache.observe(viewLifecycleOwner) {
            emplList = it
            binding?.chip1?.text = emplList[0].name
            binding?.chip2?.text = emplList[1].name
            binding?.chip3?.text = emplList[2].name
            binding?.chip4?.text = emplList[3].name
            data = args.request
            cacheData = data
            if (savedInstanceState != null) {
                data = savedInstanceState.getParcelable<RequestEmployer>("1")
                    ?: RequestEmployer("", "", false)
            }
            initialization(data)
        }
        binding?.applyButton?.setOnClickListener {
            val text = binding?.editableFilter?.text.toString()
            val flag = when (binding?.chipsGroup2?.checkedChipId) {
                R.id.chip21 -> true
                else -> false
            }
            val array = binding?.chipsGroup1?.checkedChipIds
            val type = convertListToString(array)
            data = RequestEmployer(text, type, flag)
            findNavController().navigate(ThirdFragmentDirections.actionThirdFragmentToSecondFragment(data))
        }
        binding?.topAppBarFilter?.setNavigationOnClickListener {
            findNavController().navigate(ThirdFragmentDirections.actionThirdFragmentToSecondFragment(cacheData))
        }
        binding?.topAppBarFilter?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.clear_filter -> {
                    data = RequestEmployer("", "", false)
                    initialization(data)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val text = binding?.editableFilter?.text.toString()
        val flag = when (binding?.chipsGroup2?.checkedChipId) {
            R.id.chip21 -> true
            else -> false
        }
        val array = binding?.chipsGroup1?.checkedChipIds
        val type = convertListToString(array)
        data = RequestEmployer(text, type, flag)
        outState.putParcelable("1", data)
    }

    private fun initialization(request: RequestEmployer) {
        if (request.flag) {
            binding?.chip21?.isChecked = true
        } else {
            binding?.chip22?.isChecked = true
        }
        binding?.chip1?.isChecked = request.type.contains(emplList[0].id)
        binding?.chip2?.isChecked = request.type.contains(emplList[1].id)
        binding?.chip3?.isChecked = request.type.contains(emplList[2].id)
        binding?.chip4?.isChecked = request.type.contains(emplList[3].id)
        binding?.editableFilter?.setText(data.text)
    }

    private fun convertListToString(list: List<Int>?): String {
        if (list.isNullOrEmpty()) return ""
        val strings = list.map { id ->
            when (id) {
                R.id.chip1 -> emplList[0].id
                R.id.chip2 -> emplList[1].id
                R.id.chip3 -> emplList[2].id
                R.id.chip4 -> emplList[3].id
                else -> IllegalArgumentException()
            }
        }
        return strings.joinToString(separator = "&")
    }
}