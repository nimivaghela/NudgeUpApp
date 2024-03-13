package com.app.collt.ui.auth.sync_contact

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.collt.databinding.ItemInviteFriendsBinding
import com.app.collt.domain.models.ContactModel
import com.app.collt.extension.layoutInflater

class ContactListAdapter(private val contactModelArrayList: MutableList<ContactModel>) :
    RecyclerView.Adapter<ContactListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder.from(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            val data = contactModelArrayList[position]
            tvUserName.text = data.firstName
        }
    }

    override fun getItemCount() = contactModelArrayList.size

    class MyViewHolder(var binding: ItemInviteFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val binding =
                    ItemInviteFriendsBinding.inflate(parent.context.layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}