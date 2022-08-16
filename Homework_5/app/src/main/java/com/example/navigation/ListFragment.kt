package com.example.navigation

import android.content.res.Configuration
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.adapter.ContactAdapter
import com.example.navigation.data.Contact
import com.example.navigation.databinding.ListFragmentBinding
import com.example.navigation.listener.Listener
import com.example.navigation.util.withArguments

class ListFragment : Fragment(R.layout.list_fragment) {

    private var binding: ListFragmentBinding? = null
    private var contactAdapter: ContactAdapter? = null
    private var data = emptyList<Contact>()
    private val listener: Listener?
        get() = activity?.let { it as Listener }
    companion object {
        const val LIST_KEY = "list"
        fun newInstance(data : List<Contact>) : ListFragment {
            return ListFragment().withArguments {
                putParcelableArray(LIST_KEY, data.toTypedArray())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.listContact?.apply {
            contactAdapter = ContactAdapter{position -> showDetail(position)}
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        data = (arguments?.getParcelableArray(LIST_KEY)?.toList() as List<Contact>)
        contactAdapter?.list = data
    }

    private fun showDetail(position: Int) {
        listener?.showDetailFragment(data[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        contactAdapter = null
    }
}