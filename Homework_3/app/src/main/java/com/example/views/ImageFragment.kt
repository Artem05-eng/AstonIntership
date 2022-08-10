package com.example.views

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.views.databinding.SecondFragmentBinding

class ImageFragment : Fragment() {

    private var binding: SecondFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SecondFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.editLink?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Glide.with(requireContext())
                    .load(p0?.toString())
                    .placeholder(R.drawable.ic_image)
                    .centerCrop()
                    .error(Toast.makeText(requireContext(), "Error download!", Toast.LENGTH_SHORT).show())
                    .into(binding!!.image)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}