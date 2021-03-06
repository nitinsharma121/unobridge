package com.provider.unobridge.ui.fragments.profile.dialogs

import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.databinding.ItemContactBinding
import com.provider.unobridge.room.entities.PromotoinsData

class PromotoinsAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<ItemContactBinding, PromotoinsData>() {


    override fun bind(holder: ViewHolder, item: PromotoinsData, position: Int) {

        if (item.type == "Job") {
            holder.binding.tvContact.text = item.description
            holder.binding.tvName.text = item.title
        } else {
            holder.binding.tvContact.text = "Ends on ${item.expireDate}"
            holder.binding.tvName.text = "Promotion Discount ${item.discountValue}%"
        }


    }


}