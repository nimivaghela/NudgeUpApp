package com.app.collt.ui.auth.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.collt.databinding.ItemGroupChatBinding
import com.app.collt.extension.layoutInflater

class HomeListDataAdapter(
    private val text: (String) -> Unit,
) : RecyclerView.Adapter<HomeListDataAdapter.UserViewHolder>()/*(DiffCallback)*/ {
    var list: MutableList<String> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder.from(parent)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
//            text(list[position])
        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    class UserViewHolder(var binding: ItemGroupChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val binding =
                    ItemGroupChatBinding.inflate(parent.context.layoutInflater, parent, false)
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

    interface OnItemClicked{

    }
}