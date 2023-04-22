package com.evince.useroperations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.evince.useroperations.databinding.ItemUserBinding
import com.evince.useroperations.models.UserModel

// using ListAdapter for better performance
class UsersAdapter(val listener:(UserModel)->Unit) : ListAdapter<UserModel, UsersAdapter.ItemViewHolder>(
    // using AsyncDifferConfig to compare items and contents of items in list for better performance and to update only changed items
    AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }).build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // passing model to view holder
        holder.bindData(currentList[position])
    }

    inner class ItemViewHolder(private val binding: ItemUserBinding) : ViewHolder(binding.root) {
        fun bindData(userModel: UserModel) {
            binding.model = userModel

            binding.root.setOnClickListener {
                listener.invoke(userModel)
            }
        }
    }
}