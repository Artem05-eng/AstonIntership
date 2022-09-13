package com.example.studio42.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studio42.databinding.CardVacancyBinding
import com.example.studio42.domain.entity.Vacancy

class VacancyAdapter(
    private val callback1: (String) -> Unit
) : RecyclerView.Adapter<VacancyViewHolder>() {

    var list = emptyList<Vacancy>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = CardVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding, callback1 = callback1)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

class VacancyViewHolder(
    private val binding: CardVacancyBinding,
    private val callback1: (link: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Vacancy) {
        binding.vacancyName.text = data.name
        binding.vacancyLink.text = data.alternate_url
        binding.vacancyLink.setOnClickListener {
            callback1(data.alternate_url)
        }
    }
}