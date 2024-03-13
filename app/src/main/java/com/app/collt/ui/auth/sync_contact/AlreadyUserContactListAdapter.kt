package com.app.collt.ui.auth.sync_contact

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.collt.databinding.ItemAlreadyUserOfNudgeupBinding
import com.app.collt.domain.models.response.GetSyncUserData
import com.app.collt.domain.models.response.UserMedia
import com.app.collt.extension.gone
import com.app.collt.extension.layoutInflater
import com.app.collt.extension.setImageWithUrl
import com.app.collt.extension.visible

class AlreadyUserContactListAdapter(private val contactModelArrayList: ArrayList<GetSyncUserData>) :
    RecyclerView.Adapter<AlreadyUserContactListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder.from(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            val data = contactModelArrayList[position]
            tvUserName.text = data.userName
            tvContactName.text = data.name

            data.userMedia?.let {
                if (it.size > 0) {
                    val userMedia: UserMedia = it[it.size - 1]
                    setImageWithUrl(
                        holder.itemView.context,
                        holder.binding.ivProfile,
                        userMedia.media
                    )
                }
            }

            if (data.userCategory?.status == "APPROVE") {
                holder.binding.ivProfileApprove.visible()
            } else {
                holder.binding.ivProfileApprove.gone()
            }
        }
    }


    override fun getItemCount() = contactModelArrayList.size

    class MyViewHolder(var binding: ItemAlreadyUserOfNudgeupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val binding = ItemAlreadyUserOfNudgeupBinding.inflate(
                    parent.context.layoutInflater, parent, false
                )
                return MyViewHolder(binding)
            }
        }
    }
}