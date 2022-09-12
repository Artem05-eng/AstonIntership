package com.example.studio42.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studio42.databinding.LoadStateLayoutBinding

class LoaderStateAdapter :
    LoadStateAdapter<LoaderStateAdapter.EmployerLoaderStateViewHolder>() {

    class EmployerLoaderStateViewHolder(
        private val binding: LoadStateLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.loadStateErrorMessage.isVisible = loadState is LoadState.Error
            binding.loadStateProgress.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: EmployerLoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): EmployerLoaderStateViewHolder {
        val binding =
            LoadStateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployerLoaderStateViewHolder(binding)
    }

}