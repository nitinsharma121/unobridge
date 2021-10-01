package com.provider.unobridge.ui.fragments.home

import android.view.ViewGroup
import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.data.responeClasses.ResponseStatesItem
import com.provider.unobridge.databinding.StateItemBinding

/**
 * Created by Nitin SHarma on 9/12/2021.
 */
class StatesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<StateItemBinding, ResponseStatesItem>() {

    var listenerState: clickListenerState? = null

    override fun bind(holder: ViewHolder, item: ResponseStatesItem, position: Int) {
        val marginParams: ViewGroup.MarginLayoutParams =
            holder.binding.clView.layoutParams as ViewGroup.MarginLayoutParams
        if (position % 2 == 0) {
            marginParams.setMargins(0, 0, 20, 30)
        } else marginParams.setMargins(0, 0, 0, 30)

        holder.binding.item = item

        holder.itemView.setOnClickListener {
            listenerState?.onClickState(item.name.toString(), position)
        }
    }

    interface clickListenerState {
        fun onClickState(stateName: String, position: Int)
    }
}