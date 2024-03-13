package com.app.collt.ui.auth.choose_username

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.collt.databinding.ItemSuggestionsBinding
import com.app.collt.extension.layoutInflater

class UserListAdapter(
    private val text: (String) -> Unit,

    ) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>()/*(DiffCallback)*/ {
    var list: MutableList<String> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder.from(parent)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.tvSuggestions.text = list[position]

        holder.binding.root.setOnClickListener {
            text(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class UserViewHolder(var binding: ItemSuggestionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val binding =
                    ItemSuggestionsBinding.inflate(parent.context.layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }

    fun updateList(updateList: MutableList<String>) {
        list = updateList.distinct() as MutableList<String>

        notifyDataSetChanged()
    }
    /*companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }*/
}