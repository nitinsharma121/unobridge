package com.provider.unobridge.ui.fragments.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment(), ClickListener {

    private lateinit var mBinding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        mBinding.clickHandler = this
        return mBinding.root
    }

    override fun onSetupComplete(view: View) {
        view.disableMultiTap()
        mBinding.isSetupComplete = true
    }

    override fun previewWebsite(view: View) {
        view.disableMultiTap()
        val url = "https://www.reddit.com/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun addOffer(view: View) {
        view.disableMultiTap()
        findNavController().navigate(R.id.addOfferFragment)
    }

    override fun uploadImage(view: View) {
        view.disableMultiTap()

    }

    override fun addVideo(view: View) {
        view.disableMultiTap()
    }

    override fun addJobs(view: View) {
        view.disableMultiTap()
        findNavController().navigate(R.id.addJobFragment)
    }


}