package com.provider.unobridge.ui.fragments.profile

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import carbon.dialog.ProgressDialog
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.OrganisationDetailsFragmentBinding
import com.provider.unobridge.room.entities.ProfileData
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class OrganisationDetailsFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: OrganisationDetailsFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel
    var progressDialog: ProgressDialog? = null
    var profileData = ProfileData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding =
                OrganisationDetailsFragmentBinding.inflate(inflater, container, false).apply {
                    clickHandler = ClickHandler()
                }
            mBinding.clickHandler = this.ClickHandler()
        }
        (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()
        setUpUI()
        profileData = arguments?.get(getString(R.string.profile_params)) as ProfileData
        return mBinding.root
    }


    private fun setUpUI() {
        progressDialog = ProgressDialog(requireContext(), R.style.ThemeOverlay_AppCompat)
        progressDialog?.setText("Fetching...")

    }

    inner class ClickHandler : AuthHandlerActivity.onClickListeners {

        override fun onClickNext() {
            findNavController().navigate(
                R.id.digital_sites_fragment, bundleOf(
                    getString(R.string.profile_params) to profileData
                )
            )
        }

        override fun onClickBack() {
            findNavController().navigateUp()
        }

        fun onClickManually() {
            Log.e("OnClickManually", "OnClick")
            mBinding.nsParent.visibility = VISIBLE
            mBinding.clOptions.visibility = GONE
        }

        fun onClickFetch() {
            mBinding.nsParent.visibility = GONE
            mBinding.clOptions.visibility = VISIBLE
            mBinding.clGstin.visibility = VISIBLE
            mBinding.clModes.visibility = GONE
        }

        fun fetchGSTIN() {
            progressDialog?.show()
            Handler().postDelayed({
                progressDialog?.dismiss()
                findNavController().navigate(
                    R.id.digital_sites_fragment, bundleOf(
                        getString(R.string.profile_params) to profileData
                    )
                )
            }, 2000)
        }


    }


    override fun stopBlur() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }


}