package com.provider.unobridge.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.PhoneAuthProvider
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.OtpFragmentBinding
import com.provider.unobridge.providers.firebasePhoneAuth.CodeSentStatus
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthPhone
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthResult
import com.provider.unobridge.providers.firebasePhoneAuth.SignInResult
import com.provider.unobridge.ui.activities.dashboard.DashboardActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class OtpVerificationFragment : ScopedFragment(), FirebaseAuthResult, KodeinAware {

    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    val authImpl: FirebaseAuthPhone by instance()
    private val handler = Handler()
    lateinit var mBinding: OtpFragmentBinding
    private var runnable: Runnable? = null
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel

    private var verificationCode: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        authImpl.setActivity(requireActivity() as AppCompatActivity)
        mBinding = OtpFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }

        init()
        mBinding.btnLogin.isEnabled = false
        return mBinding.root

    }

    private fun init() {
        authImpl.firebaseAuthResult = this
        verificationCode = arguments?.getString(getString(R.string.verification_id))
        resendToken =
            arguments?.get(getString(R.string.resend_token)) as PhoneAuthProvider.ForceResendingToken?


        mBinding.pvOTP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mBinding.btnLogin.isEnabled = s?.length == 6

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    inner class ClickHandler {
        fun backPress() {
            findNavController().navigateUp()
        }

        fun onClickOTPSend() {
            startActivity(Intent(requireContext(), DashboardActivity::class.java))

//            if (mBinding.clDone.isVisible) {
//                findNavController().navigate(R.id.user_details_fragment)
//            } else {
//               /* mBinding.clMobile.visibility = GONE
//                mBinding.clDone.visibility = VISIBLE
//                mBinding.btnLogin.text = "Setup"*/
//
//            }

        }


    }


    override fun stopBlur() {
    }

    override fun onCodeSentResult(status: CodeSentStatus) {
        when (status) {
            is CodeSentStatus.OnSuccess -> {
                mBinding.tvResend.isEnabled = false
                verificationCode = status.verificationId
                resendToken = status.token


            }
            is CodeSentStatus.OnFailure -> showToast(status.message)

        }
    }

    override fun signInResult(result: SignInResult) {
        when (result) {
            is SignInResult.OnSuccess -> {
                var user = result.user
                mViewModel.loginParams.apply {
                    mobileNo = arguments?.getString(getString(R.string.mobile_number))
                }
                mViewModel.socialLogin()

            }
            is SignInResult.OnFailure -> showToast(result.message)

        }
    }


}
