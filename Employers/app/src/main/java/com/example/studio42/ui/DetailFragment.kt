package com.example.studio42.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.studio42.R
import com.example.studio42.databinding.DetailEmployerFragmentBinding
import com.example.studio42.presentation.DetailViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel
    private var binding: DetailEmployerFragmentBinding? = null
    private val args: DetailFragmentArgs by navArgs()
    private var urlCompany = ""
    private var urlVacancies = ""

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailEmployerFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
        val id = args.data
        viewModel.getDetail(id)
        viewModel.error.observe(viewLifecycleOwner) {
            binding?.cardLogo?.isVisible = false
            binding?.errorTextDetail?.isVisible = true
            binding?.companyLogo?.isVisible = false
            binding?.topAppBarDetail?.title = "Детальная информация"
            binding?.companyLink?.isVisible = false
            binding?.retryButtonDetail?.isVisible = true
            Toast.makeText(requireContext(), "Ошибка сети! Попробуйте снова!", Toast.LENGTH_SHORT)
                .show()
        }
        viewModel.detail.observe(viewLifecycleOwner) { detail ->
            binding?.cardLogo?.isVisible = true
            binding?.companyTrust?.isVisible = detail.trusted
            binding?.companyLogo?.isVisible = true
            binding?.companyLink?.isVisible = true
            binding?.retryButtonDetail?.isVisible = false
            binding?.topAppBarDetail?.title = detail.name.uppercase()
            binding?.errorTextDetail?.isVisible = false
            binding?.descriptionCompany?.text = detail.description
            binding?.vacancies?.text = "Открытых вакансий: ${detail.open_vacancies}"
            binding?.areaInfo?.text = "Место расположения: ${detail.area.name}"
            if (detail.logo_urls != null) {
                Glide.with(requireContext())
                    .load(detail.logo_urls.original)
                    .placeholder(R.drawable.ic_image)
                    .into(binding?.companyLogo!!)
            } else {
                binding?.companyLogo?.isGone = true
            }
            var industriesInfo = "Сфера деятельности: \n"
            detail.industries.forEach { industries ->
                industriesInfo += "\t" + industries.name + "\n"
            }
            binding?.industriesInfo?.text = industriesInfo
            urlCompany = detail.site_url
            urlVacancies = detail.vacancies_url
        }
        binding?.topAppBarDetail?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding?.companyLink?.setOnClickListener {
            if (urlCompany != "") {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlCompany))
                startActivity(browserIntent)
            }
        }
        binding?.vacancies?.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToVacancyFragment(id))
        }
        binding?.retryButtonDetail?.setOnClickListener {
            viewModel.getDetail(id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}