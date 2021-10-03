package com.provider.unobridge.ui.fragments.profile

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.UserDetailsFragmentBinding
import com.provider.unobridge.room.entities.ProfileData
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class UserDetailsFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: UserDetailsFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel
    var profileData = ProfileData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = UserDetailsFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }
        (requireActivity() as AuthHandlerActivity).mBinding.btnNext.text = "Next"
        (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()

        return mBinding.root
    }


    inner class ClickHandler : AuthHandlerActivity.onClickListeners {


        override fun onClickNext() {
            if (mBinding.etName.text.isNotEmpty() && mBinding.etPan.text.isNotEmpty() && mBinding.etEmail.text.isNotEmpty()) {
                profileData.aadharNo = mBinding.etEmail.text.toString()
                profileData.name = mBinding.etName.text.toString()
                profileData.panNo = mBinding.etPan.text.toString()

                findNavController().navigate(
                    R.id.organisation_details_fragment,
                    bundleOf(getString(R.string.profile_params) to profileData)
                )


            }

        }

        override fun onClickBack() {

        }


    }


    override fun stopBlur() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }


    private fun isValidEmailId(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}