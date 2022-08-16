package com.example.navigation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navigation.R
import com.example.navigation.data.Contact
import com.example.navigation.databinding.ContactItemBinding

class ContactAdapter(
    private val callback: (position: Int)->Unit
) : RecyclerView.Adapter<ContactHolder>() {

    var list = emptyList<Contact>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactHolder(callback, binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

class ContactHolder(
    private val callback: (position: Int) -> Unit,
    private val binding: ContactItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            callback(adapterPosition)
        }
    }

    fun onBind(data: Contact) {
        binding.nameContact.text = data.name
        binding.phoneContact.text = data.phone
        Glide.with(binding.root)
            .load(data.photoUri)
            .placeholder(R.drawable.ic_baseline_contact_phone_24)
            .into(binding.photoContact)
    }
}