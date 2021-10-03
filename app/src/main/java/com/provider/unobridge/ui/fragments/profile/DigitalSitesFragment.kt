package com.provider.unobridge.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.DigitalSitesFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.ProfileData
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.activities.MainActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class DigitalSitesFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: DigitalSitesFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel
    var isEditingMode = true
    var profileData = ProfileData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = DigitalSitesFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }

        profileData = arguments?.get(getString(R.string.profile_params)) as ProfileData

        (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()

        setUpUI()

        return mBinding.root
    }

    private fun setUpUI() {

    }


    suspend fun roomFun(): Long {
        return CalculatorDatabase.getDatabase(requireContext()).getProfileDao()
            .saveData(profileData)
    }

    private fun myFun() {
        lifecycleScope.launch { // coroutine on Main
            val data = roomFun() // coroutine on IO
            Log.e("Idddd", "${data}")
            Prefs.init().userId = data.toString()
            Prefs.init().isProfileCompleted = "1"
            (requireActivity() as AuthHandlerActivity).finish()
            startActivity(Intent(requireActivity(), MainActivity::class.java))

        }
    }

    inner class ClickHandler : AuthHandlerActivity.onClickListeners {


        override fun onClickNext() {
            if (mBinding.eBusinessName.text.isNotEmpty()) {
                if (!mBinding.clProcess.isVisible) {
                    profileData.companyName = mBinding.eBusinessName.text.toString()

                    (requireActivity() as AuthHandlerActivity).mBinding.ivBack.visibility = GONE
                    (requireActivity() as AuthHandlerActivity).mBinding.btnNext.text = "Done"
                    (requireActivity() as AuthHandlerActivity).mBinding.btnNext.isEnabled = false
                    mBinding.clProcess.visibility = VISIBLE
                    mBinding.nvParent.visibility = GONE
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            mBinding.lpWebsite.setProgressPercentage(
                                50.00,
                                true
                            )

                            object : CountDownTimer(2000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    mBinding.lpBusiness.setProgressPercentage(
                                        50.00,
                                        true
                                    )

                                }

                                override fun onFinish() {
                                    mBinding.lpBusiness.setProgressPercentage(
                                        100.00,
                                        true
                                    )
                                }
                            }.start()

                        }

                        override fun onFinish() {
                            mBinding.lpWebsite.setProgressPercentage(
                                100.00,
                                true
                            )


                            object : CountDownTimer(2000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    mBinding.lpOperation.setProgressPercentage(
                                        50.00,
                                        true
                                    )

                                }

                                override fun onFinish() {
                                    mBinding.lpOperation.setProgressPercentage(
                                        100.00,
                                        true
                                    )
                                    (requireActivity() as AuthHandlerActivity).mBinding.btnNext.isEnabled =
                                        true

                                }
                            }.start()
                        }


                    }.start()
                } else {
                    myFun()


                }
            }

        }

        override fun onClickBack() {
            findNavController().navigateUp()
        }


    }


    override fun stopBlur() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }


}