package com.example.studio42.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studio42.databinding.CardWorkBinding
import com.example.studio42.domain.entity.Employer

class EmployerAdapter(
    private val onItemClicked: (id: String) -> Unit
) : PagingDataAdapter<Employer, EmployerHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployerHolder {
        val binding = CardWorkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployerHolder(onItemClicked, binding)
    }

    override fun onBindViewHolder(holder: EmployerHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class EmployerHolder(
    private val onItemClicked: (id: String) -> Unit,
    private val binding: CardWorkBinding,
) : RecyclerView.ViewHolder(binding.root) {

    /*init {
        binding.root.setOnClickListener {
            onItemClicked(absoluteAdapterPosition)
        }
    }*/

    fun bind(data: Employer) {
        binding.companyName?.text = data.name
        binding.vacancyInfo?.text = "Вакансий в компании: ${data.open_vacancies}"
        Glide.with(binding.root)
            .load(data.logo_urls?.original)
            .centerCrop()
            .into(binding.companyLogo)
        binding.root.setOnClickListener {
            onItemClicked(data.id)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Employer>() {
    override fun areItemsTheSame(oldItem: Employer, newItem: Employer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Employer, newItem: Employer): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.open_vacancies == newItem.open_vacancies &&
                oldItem.logo_urls?.original == newItem.logo_urls?.original
    }
}