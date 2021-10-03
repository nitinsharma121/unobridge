package com.provider.unobridge.ui.fragments.profile.dialogs

import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.data.SitesData
import com.provider.unobridge.databinding.SiteItemBinding

class SitesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<SiteItemBinding, SitesData>() {

    var listener: onClick? = null

    override fun bind(holder: ViewHolder, item: SitesData, position: Int) {

        holder.binding.tvContact.text = item.url
        holder.binding.tvName.text = item.title

        holder.itemView.setOnClickListener {
            listener?.onClickItem()
        }

    }

    interface onClick {
        fun onClickItem()
    }


}