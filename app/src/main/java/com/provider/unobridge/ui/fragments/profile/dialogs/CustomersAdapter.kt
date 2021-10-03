package com.provider.unobridge.ui.fragments.profile.dialogs

import android.view.View.GONE
import android.view.View.VISIBLE
import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.databinding.ItemContactBinding
import com.provider.unobridge.room.entities.CustomersData

class CustomersAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<ItemContactBinding, CustomersData>() {


    override fun bind(holder: ViewHolder, item: CustomersData, position: Int) {

        holder.binding.tvContact.text = item.mobileNo
        holder.binding.tvName.text = item.name
        if (item.isSelected) {
            holder.binding.ivDone.visibility = VISIBLE

        } else {
            holder.binding.ivDone.visibility = GONE

        }
        holder.itemView.setOnClickListener {
            for (element in list) {
                element.isSelected = false
            }
            list[position].isSelected = true
            notifyDataSetChanged()
        }
    }

}