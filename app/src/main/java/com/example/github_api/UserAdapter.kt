package com.example.github_api

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.github_api.API.MODEL.Item
import com.example.github_api.databinding.RowUsersBinding


/**
 * Adapter for the RecyclerView in the Main Activity.
 * This adapter is responsible for displaying the list of users.
 *
 * @property listener A function that use when a user item is clicked.
 */
class UserAdapter(private val listener: (Item) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // The list of users to display
    private val data = mutableListOf<Item>()

    /**
     * Updates the list of users and notifies the RecyclerView that the data has changed.
     *
     * @param newData The new list of users.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Item>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an
     * adapter position.
     * @param viewType The view type of the new View. Int value
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
            RowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    /**
     * Returns the total number of items in the data set hold by the adapter.
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = data.size

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item
     * at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     *
     * @property binding The RowUsersBinding for this ViewHolder.
     */
    inner class UserViewHolder(private val binding: RowUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the user data to the item view. So that it can be displayed in the RecyclerView.         *
         */
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