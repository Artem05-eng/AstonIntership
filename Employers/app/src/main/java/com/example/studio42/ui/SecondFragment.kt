package com.example.studio42.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studio42.R
import com.example.studio42.databinding.SecondScreenBinding
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.presentation.SecondViewModel
import com.example.studio42.ui.adapter.EmployerAdapter
import com.example.studio42.ui.adapter.LoaderStateAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class SecondFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SecondViewModel
    private var binding: SecondScreenBinding? = null
    private val args: SecondFragmentArgs by navArgs()
    private var listAdapter: EmployerAdapter? = null
    private var requestBody = RequestEmployer("", "", false)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SecondScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[SecondViewModel::class.java]
        requestBody = args?.requestData
        listener(requestBody)
        binding?.filterButton?.setOnClickListener {
            findNavController().navigate(
                SecondFragmentDirections.actionSecondFragmentToThirdFragment(requestBody)
            )
        }
        binding?.resultList?.apply {
            listAdapter = EmployerAdapter { id -> showDetail(id) }
            adapter = listAdapter?.withLoadStateHeaderAndFooter(
                header = LoaderStateAdapter(),
                footer = LoaderStateAdapter()
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
        listAdapter?.addLoadStateListener { state ->
            val error =
                (state.refresh == LoadState.Error(Throwable())) || (state.append == LoadState.Error(
                    Throwable()
                ))
            binding?.resultList?.isVisible = (state.refresh != LoadState.Loading) && !error
            binding?.progressList?.isVisible = state.refresh == LoadState.Loading
            binding?.errorTextList?.isVisible = error
            binding?.retryButtonList?.isVisible = error
        }
        binding?.textField?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                requestBody.text = p0.toString()
                updateList()
                if (p0.toString().isNotEmpty()) {
                    viewModel.checkFlag()
                } else {
                    viewModel.uncheckFlag()
                }
            }
        })
        binding?.chip?.setOnCloseIconClickListener {
            requestBody = RequestEmployer("", "", false)
            updateList()
            it.isGone = true
            binding?.textField?.editText?.setText("")
            viewModel.uncheckFlag()
        }
        viewModel.flagFilter.observe(viewLifecycleOwner) { flag ->
            if (flag) {
                binding?.chip?.isGone = false || !isOnline()
                binding?.filterButton?.setBackgroundColor(resources.getColor(R.color.blue_42))
                binding?.filterButton?.setImageDrawable(resources.getDrawable(R.drawable.filter_light))
            } else {
                binding?.chip?.isGone = true
                binding?.filterButton?.setBackgroundColor(resources.getColor(R.color.grey_42))
                binding?.filterButton?.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_filter_alt_24))
            }
        }
        viewModel.count.observe(viewLifecycleOwner) {
            binding?.chip?.text = "Найдено ${it} работодателей"
        }
        binding?.retryButtonList?.setOnClickListener {
            updateList()
        }
        updateList()
    }

    private fun updateList() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.getEmploers(requestBody, isOnline()).collectLatest { pagingData ->
                listAdapter?.submitData(pagingData)
            }
        }
    }

    fun isOnline(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        listAdapter = null
    }

    private fun listener(data: RequestEmployer) {
        requestBody = data
        if (data != RequestEmployer("", "", false)) {
            viewModel.checkFlag()
        } else {
            viewModel.uncheckFlag()
        }
        binding?.textField?.editText?.setText(data.text)
    }

    private fun showDetail(id: String) {
        findNavController().navigate(
            SecondFragmentDirections.actionSecondFragmentToDetailFragment(
                id
            )
        )
    }
}