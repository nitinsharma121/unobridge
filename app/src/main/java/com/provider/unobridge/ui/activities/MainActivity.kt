package com.provider.unobridge.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedActivity
import com.provider.unobridge.databinding.ActivityMainBinding
import com.provider.unobridge.providers.payment.PaymentEventHandler
import com.provider.unobridge.room.CalculatorDatabase

class MainActivity : ScopedActivity(), NavController.OnDestinationChangedListener,
    com.provider.unobridge.providers.payment.PaymentResultListener {
    lateinit var mBinding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    val paymentEventHandler = PaymentEventHandler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
        setStatusBarColor(R.color.gray_view_color)

        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        mBinding.clickHandler = this.ClickHandler()
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_dash_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this)
        ClickHandler().goToHome()


        var profileData = CalculatorDatabase.getDatabase(this).getProfileDao()
            .getProfileData(Prefs.init().userId)
        Log.e("ProfileDataa", "${profileData}")
        mBinding.atCompany.setText( profileData.companyName.toString())
    }


    inner class ClickHandler {

        fun goToHome() {
            navController.navigate(R.id.home_fragment)
        }

        fun goToNotifications() {
//            navController.navigate(R.id.notifications_fragment)
        }

    }

    private fun changeIcons(id: Int?) {
        mBinding.apply {
            bDashboard.setImageResource(R.drawable.ic_home)
            bNotifications.setImageResource(R.drawable.ic_bell)
            bSearch.setImageResource(R.drawable.ic_baseline_search_24)

        }

        when (id) {
            R.id.home_fragment -> {
                mBinding.apply {
                    bDashboard.setImageResource(R.drawable.ic_home_white)
                }
            }

            R.id.notifications_fragment -> {
                mBinding.bNotifications.setImageResource(R.drawable.ic_bell_selected)
            }

        }
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        changeIcons(destination.id)
        when (destination.id) {
            R.id.rides_fragment, R.id.account_fragment, R.id.aadhar_details_fragment, R.id.vehicle_details_fragment -> {
                mBinding.bottomNav.visibility = GONE

            }
            else -> {
                mBinding.bottomNav.visibility = VISIBLE

            }
        }

    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.home_fragment) {
            super.onBackPressed()
        } else if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }


    override fun onSuccess(result: String?) {
    }

    override fun onFailure(result: String?) {
    }


    fun purchaseSubscription() {

    }
}


