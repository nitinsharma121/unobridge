package com.provider.unobridge.ui.fragments.profile

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.UserTypeFragmentBinding
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class UserTypeFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    lateinit var mBinding: UserTypeFragmentBinding
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = UserTypeFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
            initToolbar(
                mBinding.tToolbar,
                getString(R.string.user_type),
                getString(R.string.enter_otp_msg)
            )
            mBinding.tToolbar.tvSubTitle.text =
                Html.fromHtml("Let us know your " + "<font color=#0068FF>" + "who you are" + "</font>" + ".")
            this.ClickHandler().onClickAgent()

        }
        (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()

        return mBinding.root
    }


    inner class ClickHandler : AuthHandlerActivity.onClickListeners {
        override fun onClickNext() {
            mViewModel.profileParams.userType = if (mBinding.ivAgent.isSelected) getString(
                R.string.agent
            ) else getString(R.string.driver)
            findNavController().navigate(
                R.id.user_details_fragment,
                bundleOf(
                    getString(R.string.profile_params) to  mViewModel.profileParams
                )
            )
        }

        fun onClickDriver() {
            mBinding.tvDriver.isSelected = true
            mBinding.ivDriver.isSelected = true
            mBinding.tvAgent.isSelected = false
            mBinding.ivAgent.isSelected = false

        }

        fun onClickAgent() {
            mBinding.tvDriver.isSelected = false
            mBinding.ivDriver.isSelected = false
            mBinding.tvAgent.isSelected = true
            mBinding.ivAgent.isSelected = true
        }
    }


    override fun stopBlur() {

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}