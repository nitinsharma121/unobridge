package com.provider.unobridge.ui.fragments.dashboard

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.base.Utils.Companion.openBrowser
import com.provider.unobridge.databinding.FragmentDashboardBinding
import com.provider.unobridge.databinding.LayoutDashboardStepsBinding
import com.provider.unobridge.ui.activities.dashboard.DashboardActivity


class DashboardFragment : Fragment(), ClickListener {

    private lateinit var mBinding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        mBinding.clickHandler = this
        readArguments()
        setupUI()
        return mBinding.root
    }

    fun setupUI() {
        mBinding.iCreateWebsite.updateField {
            mBinding.iCreateWebsite.vHR.visibility = VISIBLE
            mBinding.iSelectBusiness.updateField {
                mBinding.iSelectBusiness.vHR.visibility = VISIBLE
                mBinding.iManageOperation.updateField { }
            }
        }

        /*val bindings = arrayOf(
            mBinding.iCreateWebsite,
            mBinding.iSelectBusiness,
            mBinding.iManageOperation
        )
        bindings.forEachIndexed { i, b ->
            b.updateField{
                runWithDelay(500) {
                    if (i > 2)
                        bindings[i].vHR.visibility = VISIBLE
                }
            }
        }*/
    }

    private fun LayoutDashboardStepsBinding.updateField(work: () -> Unit) {
        root.visibility = VISIBLE
        avProgress.playAnimation()
        avProgress.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                work()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}

        })
    }

    private fun readArguments() {
        val updated = arguments?.getBoolean(getString(R.string.key_updated_dashboard))
        updated?.let {
            if(::mBinding.isInitialized) {
                mBinding.isSetupComplete = it
                (requireActivity() as DashboardActivity).changeHeaderForDashboard()
            }
        }
    }

    override fun onSetupComplete(view: View) {
        view.disableMultiTap()
        mBinding.isSetupComplete = true
        (requireActivity() as DashboardActivity)
            .changeHeaderForDashboard()
    }

    override fun previewWebsite(view: View) {
        view.disableMultiTap()
        requireContext().openBrowser(getString(R.string.raju_electrician_website))
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

}