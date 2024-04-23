package com.example.github_api

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.github_api.API.MODEL.Item
import com.example.github_api.databinding.RowUsersBinding


class UserAdapter(private val listener: (Item) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val data = mutableListOf<Item>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Item>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        RowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    inner class UserViewHolder(private val binding: RowUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            with(binding) {
                img.load(item.avatar_url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                user.text = item.login
            }
        }
    }
}
