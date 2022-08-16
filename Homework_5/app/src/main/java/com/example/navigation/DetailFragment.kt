package com.example.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.navigation.data.Contact
import com.example.navigation.databinding.DetailFragmentBinding
import com.example.navigation.listener.Listener
import com.example.navigation.util.withArguments

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private var binding: DetailFragmentBinding? = null
    private lateinit var data: Contact
    private val listener: Listener?
        get() = activity?.let { it as Listener }

    companion object {
        const val DETAIL_KEY = "detail"
        fun newInstance(data: Contact): DetailFragment {
            return DetailFragment().withArguments {
                putParcelable(DETAIL_KEY, data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arguments?.getParcelable<Contact>(DETAIL_KEY)!!
        Glide.with(requireContext())
            .load(data.photoUri)
            .placeholder(R.drawable.ic_baseline_contact_phone_24)
            .into(binding!!.photoContactDetail)
        binding!!.nameContactDetail.text = data.name
        binding!!.phoneContactDetail.setText(data.phone)
        binding!!.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding!!.okButton.setOnClickListener {
            val contact = Contact(
                id = data.id,
                name = data.name,
                phone = binding!!.phoneContactDetail.text.toString(),
                photoUri = data.photoUri
            )
            listener?.updateListFragment(contact)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}