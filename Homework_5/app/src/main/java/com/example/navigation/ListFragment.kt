package com.example.navigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
        fun newInstance(data: List<Contact>): ListFragment {
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
            contactAdapter = ContactAdapter({ position -> showDetail(position) },
                { position -> deleteContact(position) })
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        data = (arguments?.getParcelableArray(LIST_KEY)?.toList() as List<Contact>)
        contactAdapter?.updateContacts(data)
        binding?.searchText?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val list = data.filter {(it.name.contains(p0.toString(), true))}
                contactAdapter?.updateContacts(list)
            }

        })
    }

    private fun showDetail(position: Int) {
        listener?.showDetailFragment(data[position])
    }

    private fun deleteContact(position: Int) {
        val fragment = AlertFragment.newInstance(data[position])
        fragment.show(childFragmentManager, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        contactAdapter = null
    }

    fun updateContact(data: List<Contact>) {
        contactAdapter?.updateContacts(data)
    }
}