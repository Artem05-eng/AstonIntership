package com.example.navigation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.navigation.data.Contact
import com.example.navigation.listener.Listener
import com.example.navigation.util.withArguments

class AlertFragment : DialogFragment() {

    private val listener: Listener?
        get() = activity?.let {it as Listener}

    companion object {
        private const val DATA_KEY = "data_key"
        fun newInstance(data: Contact): AlertFragment {
            return AlertFragment().withArguments {
                putParcelable(DATA_KEY, data)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val data = requireArguments().getParcelable<Contact>(DATA_KEY)
        return android.app.AlertDialog.Builder(requireActivity())
            .setTitle("Вы действительно хотите удалить контакт?")
            .setPositiveButton("ДА", { _,_ -> listener?.deleteContact(data!!)})
            .setNegativeButton("НЕТ", { _,_ ->  dismiss() })
            .create()

    }
}