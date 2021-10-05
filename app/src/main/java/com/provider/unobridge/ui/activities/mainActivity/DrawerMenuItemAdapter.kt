package com.provider.unobridge.ui.activities.mainActivity

import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.data.model.DrawerMenuItem
import com.provider.unobridge.databinding.LayoutDrawerMenuItemBinding
import com.provider.unobridge.ui.activities.dashboard.ClickListener

class DrawerMenuItemAdapter(override val layoutId: Int) : BaseRecyclerAdapter<LayoutDrawerMenuItemBinding, DrawerMenuItem>() {
    var clickHandler:ClickListener? = null
    override fun bind(holder: ViewHolder, item: DrawerMenuItem, position: Int) {
        holder.binding.item = item
        holder.binding.root.setOnClickListener {
            it.disableMultiTap()
            clickHandler?.redirect(item.navigationId)
        }
    }
}