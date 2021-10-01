package com.provider.unobridge.ui.fragments.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.EnterMobileFragmentBinding
import com.provider.unobridge.providers.firebasePhoneAuth.CodeSentStatus
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthPhone
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthResult
import com.provider.unobridge.providers.firebasePhoneAuth.SignInResult
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class EnterMobileFragment : ScopedFragment(), FirebaseAuthResult, KodeinAware {

    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    lateinit var mBinding: EnterMobileFragmentBinding
    private val authImpl: FirebaseAuthPhone by instance()
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel


    override fun stopBlur() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            authImpl.setActivity(requireActivity() as AppCompatActivity)
            mBinding = EnterMobileFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
            init()

        }
        return mBinding.root
    }

    fun init() {
        authImpl.firebaseAuthResult = this
        setupTnCAndPP()
        mBinding.tvCode.text = "+91"
    }

    inner class ClickHandler {

        fun backPress() {
            findNavController().navigateUp()
        }

        fun onClickLogin() {
            sendOtp()
        }

        fun showCountries() {
        }
    }

    fun sendOtp() {
        if (mBinding.etPhone.text.toString().isEmpty()) {
            showToast(getString(R.string.mobile_validation_empty))
        } else if (mBinding.etPhone.text.toString().length != 10) {
            showToast(getString(R.string.mobile_validation_digit))
        } else {
            showProgress()
            authImpl.sendOtp(mBinding.etPhone.text.toString(), "+91")
        }

    }


    private fun setupTnCAndPP() {
        val parentText = SpannableString(getString(R.string.t_n_c_and_privacy_policy))
        val tnCSpan: ClickableSpan = object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = resources.getColor(R.color.hint_grey)
            }

            override fun onClick(v: View) {
                openHtml(R.string.terms_amp_conditions, "file:///android_asset/terms_of_use.html")
            }
        }
        val ppSpan: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = resources.getColor(R.color.hint_grey)
            }

            override fun onClick(v: View) {
                openHtml(R.string.privacy_policy, "file:///android_asset/privacy_police_new.html")
            }
        }
        parentText.setSpan(tnCSpan, 16, 34, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        parentText.setSpan(ppSpan, 43, 57, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        mBinding.tvText2.text = parentText
        mBinding.tvText2.movementMethod = LinkMovementMethod.getInstance()
        mBinding.tvText2.highlightColor = resources.getColor(android.R.color.transparent)
    }

    private fun openHtml(title: Int, fileUrl: String) {
        /*  val bundle = bundleOf(
              getString(R.string.toolbarTitle) to getString(title),
              getString(R.string.fileURL) to fileUrl
          )
          navigate(R.id.infoFragment, bundle)*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }
        return true
    }

    override fun onCodeSentResult(status: CodeSentStatus) {
        hideProgress()
        when (status) {
            is CodeSentStatus.OnSuccess -> {
                findNavController().navigate(
                    R.id.otp_fragment, bundleOf(
                        getString(R.string.verification_id) to status.verificationId,
                        getString(R.string.resend_token) to status.token,
                        getString(R.string.country_code) to "+91",
                        getString(R.string.mobile_number) to mBinding.etPhone.text.toString()

                    )
                )
            }
            is CodeSentStatus.OnFailure -> showToast(status.message)

        }

    }

    override fun signInResult(result: SignInResult) {

    }


}