package com.provider.unobridge.ui.fragments.profile.dialogs

import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.databinding.ItemOrderBinding
import com.provider.unobridge.room.entities.OrdersData

class OrdersAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<ItemOrderBinding, OrdersData>() {


    override fun bind(holder: ViewHolder, item: OrdersData, position: Int) {

        holder.binding.tvCustomerName.text = "Customer Name : ${item.customerName}"
        holder.binding.tvEndDate.text = "End Date : ${item.endDate}"
        holder.binding.tvPrice.text = "Price : ₹ ${item.orderPrice}"


    }

}