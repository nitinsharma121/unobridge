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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.PhoneAuthProvider
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.OtpFragmentBinding
import com.provider.unobridge.providers.firebasePhoneAuth.CodeSentStatus
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthPhone
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthResult
import com.provider.unobridge.providers.firebasePhoneAuth.SignInResult
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.activities.MainActivity
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
        setupObserver()
        return mBinding.root

    }


    private fun init() {
        authImpl.firebaseAuthResult = this
        verificationCode = arguments?.getString(getString(R.string.verification_id))
        resendToken =
            arguments?.get(getString(R.string.resend_token)) as PhoneAuthProvider.ForceResendingToken?

        countDownStart()
        mBinding.pvOTP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    ClickHandler().onClickOTPSend()
                }
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
            if (mBinding.pvOTP.text.toString().length == 6) {
                mBinding.root.requestFocus()
                hideKeyboard(mBinding.root)
                mViewModel.showLoading.postValue(true)
                stopCountDown()
                val credential = PhoneAuthProvider.getCredential(
                    verificationCode.toString(), mBinding.pvOTP.text.toString()
                )
                authImpl.signInUser(credential)

            } else {
                showToast(getString(R.string.valid_otp_msg))
            }

        }

        fun onClickResentOTP() {

            authImpl.sendOtp(
                arguments?.getString(getString(R.string.mobile_number)).toString(),
                arguments?.getString(getString(R.string.country_code)).toString()
            )

        }


    }


    private fun countDownStart() {
        var secondsCount = 60
        runnable = object : Runnable {
            override fun run() {
                try {
                    handler.postDelayed(this, 1000)
                    secondsCount--
                    if (secondsCount > 0) {
                        mBinding.tvTimer.visibility = VISIBLE
                        mBinding.tvTimer.text =
                            "00:${if (secondsCount < 10) " 0$secondsCount" else secondsCount}  "
                    } else {
                        mBinding.tvResend.isEnabled = true
                        stopCountDown()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        handler.postDelayed(runnable, 0)
    }

    private fun stopCountDown() {
        mBinding.tvTimer.visibility = GONE
        handler.removeCallbacks(runnable)
    }

    override fun stopBlur() {
    }

    override fun onCodeSentResult(status: CodeSentStatus) {
        when (status) {
            is CodeSentStatus.OnSuccess -> {
                mBinding.tvTimer.visibility = GONE
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


    private fun setupObserver() {

        mViewModel.apply {
            data.observe(viewLifecycleOwner, Observer {
                Prefs.init().userId = it.data?.loginData?.id.toString()
                Prefs.init().isProfileCompleted = it.data?.loginData?.isProfileCompleted.toString()
                Prefs.init().isLogIn = "true"
                Prefs.init().accessToken = it.data?.loginData?.accessToken.toString()

                if (Prefs.init().isProfileCompleted == "1") {
                    showToast("Login Successfully, Welcome back to ${getString(R.string.app_name)}")
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    ( requireActivity() as AuthHandlerActivity).finish()


                } else {
                    showToast("Login Successfully, Please fill the required details")
                    findNavController().navigate(
                        R.id.user_type_fragment
                    )
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

}
