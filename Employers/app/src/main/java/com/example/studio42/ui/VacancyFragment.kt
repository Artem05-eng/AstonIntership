package com.example.studio42.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studio42.databinding.VacanciesLayoutBinding
import com.example.studio42.presentation.VacancyViewModel
import com.example.studio42.ui.adapter.VacancyAdapter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class VacancyFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: VacancyViewModel
    private var binding: VacanciesLayoutBinding? = null
    private val args: VacancyFragmentArgs by navArgs()
    private var listAdapter: VacancyAdapter? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VacanciesLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[VacancyViewModel::class.java]
        val id = args.data
        viewModel.getDetail(id)
        binding?.vacanciesList?.apply {
            listAdapter = VacancyAdapter { link -> showVacancyDetail(link) }
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding?.topAppBarVacancy?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding?.errorTextVacancy?.isVisible = true
            binding?.retryButtonDetail?.isVisible = true
            binding?.emptyVacancies?.isVisible = false
        }
        viewModel.data.observe(viewLifecycleOwner) {
            binding?.errorTextVacancy?.isVisible = false
            binding?.retryButtonDetail?.isVisible = false
            binding?.emptyVacancies?.isVisible = it.isEmpty()
            listAdapter?.list = it
        }
        binding?.retryButtonDetail?.setOnClickListener {
            viewModel.getDetail(id)
        }
    }

    private fun showVacancyDetail(link: String) {
        if (link != "") {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
        listAdapter = null
    }
}