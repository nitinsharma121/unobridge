package com.provider.unobridge.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedActivity
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.AuthHandlerActivityBinding

class AuthHandlerActivity : ScopedActivity(), NavController.OnDestinationChangedListener {

    lateinit var mBinding: AuthHandlerActivityBinding
    lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    var listener: onClickListeners? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.auth_handler_activity
        )
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this)



        mBinding.btnNext.setOnClickListener { listener?.onClickNext() }
        mBinding.ivBack.setOnClickListener { listener?.onClickBack() }
    }

    fun hideBlurView() {
        (navHostFragment.childFragmentManager.fragments[0] as ScopedFragment).stopBlur()
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        when (destination.id) {
            R.id.otp_fragment -> {
                mBinding.stepIndicator.visibility = View.GONE
                mBinding.clBottom.visibility = GONE
            }
            R.id.enter_mobile_fragment -> {
                mBinding.stepIndicator.visibility = View.GONE
                mBinding.clBottom.visibility = GONE
            }
            R.id.user_details_fragment -> {
                mBinding.stepIndicator.visibility = View.VISIBLE
                mBinding.clBottom.visibility = VISIBLE
                mBinding.ivBack.visibility = GONE
            }
            else -> {
                mBinding.stepIndicator.visibility = View.VISIBLE
                mBinding.clBottom.visibility = View.VISIBLE
                mBinding.ivBack.visibility = VISIBLE

            }

        }

        setProgress(destination)


        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
        setStatusBarColor(R.color.white)


    }

    fun setProgress(destination: NavDestination) {
        when (destination.id) {
            R.id.user_details_fragment -> mBinding.stepIndicator.currentStepPosition = 0
            R.id.organisation_details_fragment -> mBinding.stepIndicator.currentStepPosition = 1
            R.id.digital_sites_fragment -> mBinding.stepIndicator.currentStepPosition = 2
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.enter_mobile_fragment -> finish()
            else -> {
                if (!navController.navigateUp()) {
                    super.onBackPressed()
                }
            }

        }
    }

    interface onClickListeners {
        fun onClickNext()
        fun onClickBack()
    }

}