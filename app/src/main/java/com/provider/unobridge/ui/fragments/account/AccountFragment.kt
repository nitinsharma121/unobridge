package com.provider.unobridge.ui.fragments.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.responeClasses.ResponseProfileData
import com.provider.unobridge.databinding.AccountFragmentBinding
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.activities.MainActivity
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModel
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class AccountFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    lateinit var mBinding: AccountFragmentBinding
    private val viewModelFactory: AccountViewModelFactory by instance()
    lateinit var mViewModel: AccountViewModel
    var profileData = ResponseProfileData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(AccountViewModel::class.java)
            mBinding = AccountFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
            mBinding.shimmerFrameLayout.startShimmer()
            mViewModel.getProfile()
        }
        setupObserver()

        return mBinding.root
    }

    inner class ClickHandler {
        fun onBackPress() {
            mBinding.shimmerFrameLayout.stopShimmer()
            mBinding.shimmerFrameLayout.visibility = GONE
            findNavController().navigateUp()
        }

        fun onClickAaadharDetails() {
            findNavController().navigate(
                R.id.aadhar_details_fragment, bundleOf(
                    getString(R.string.aadhar_details) to profileData.userAadharDetails
                )
            )

        }

        fun onClickVehicleDetails() {
            findNavController().navigate(
                R.id.vehicle_details_fragment, bundleOf(
                    getString(R.string.vehicle_details) to profileData.userDriverLicenseDetails
                )
            )
        }

        fun logout() {
            logoutUser()
        }


    }


    fun logoutUser() {
        Prefs.init().clear()
        startActivity(Intent(requireActivity(), AuthHandlerActivity::class.java))
        (requireActivity() as MainActivity).finish()
        showToast("User logout successfully")
    }

    override fun stopBlur() {

    }

    private fun setupObserver() {

        mViewModel.apply {
            data.observe(viewLifecycleOwner, Observer {
                profileData = it.data?.profileData!!

                Prefs.init().profileData = profileData
                mBinding.shimmerFrameLayout.stopShimmer()
                mBinding.shimmerFrameLayout.visibility = GONE
                mBinding.clAccount.visibility = VISIBLE


                mBinding.apply {
                    image = profileData.userProfiles?.profileImage
                    ivNo.text = profileData.mobileNo
                    tvUserEmail.text = profileData.userProfiles?.email
                    tvUsername.text = "${profileData.userProfiles?.name}, ${profileData.userType}"
                }

                if (profileData.userType.equals(getString(R.string.agent), true)) {
                    mBinding.lVehicleDetails.visibility = GONE
                }

            })
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    showProgress()
                } else hideProgress()
            })

            showMessage.observe(viewLifecycleOwner, Observer {
                hideProgress()
                if (!it.isNullOrEmpty()) {
                    showToast(it)
                }

            })
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}