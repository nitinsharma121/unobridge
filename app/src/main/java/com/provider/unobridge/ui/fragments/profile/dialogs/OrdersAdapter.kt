package com.provider.unobridge.ui.fragments.profile.dialogs

import android.view.View.GONE
import android.view.View.VISIBLE
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.databinding.ItemOrderBinding
import com.provider.unobridge.room.entities.OrdersData

class OrdersAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<ItemOrderBinding, OrdersData>() {

    var listener:onClick?=null

    override fun bind(holder: ViewHolder, item: OrdersData, position: Int) {



        holder.itemView.setOnClickListener{
            listener?.onClickOrder(item, position)
        }


    }

    interface onClick{
        fun onClickOrder(item:OrdersData,position: Int)
    }

}