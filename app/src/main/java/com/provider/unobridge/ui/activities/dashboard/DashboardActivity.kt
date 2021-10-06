package com.provider.unobridge.ui.activities.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedActivity
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.data.model.DrawerMenuItem
import com.provider.unobridge.databinding.ActivityDashboardBinding
import com.provider.unobridge.providers.resources.ResourcesProvider
import com.provider.unobridge.ui.activities.mainActivity.DrawerMenuItemAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class DashboardActivity : ScopedActivity(), ClickListener, KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val resources: ResourcesProvider by instance()
    lateinit var mBinding: ActivityDashboardBinding
    lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
        setStatusBarColor(R.color.gray_view_color)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        mBinding.clickHandler = this
        mBinding.showRedirection = false
        setupNavigation()
        setupBodyMenu()
        setupFooterMenu()
        mBinding.title = "Welcome, Raju Sharma!"
        mBinding.showRedirection = false
        mBinding.tvTitle.setTextColor(resources.getColor(R.color.action_button_color))
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.dashboardFragment -> {
                    showToolbar()
                    mBinding.title = "Welcome, Raju Sharma!"
                    mBinding.showRedirection = false
                    mBinding.tvTitle.setTextColor(resources.getColor(R.color.action_button_color))
                }
                R.id.customizeWebsiteFragment -> {
                    mBinding.cvToolbar.visibility = GONE
                    mBinding.fabSupport.visibility = GONE
                }
                R.id.orders_fragment -> {
                    showToolbar()
                    mBinding.showRedirection = true
                    mBinding.title = "Order Management"
                }
                R.id.addOrderFragment -> {
                    showToolbar()
                    mBinding.showRedirection = true
                    mBinding.title = "New Order"
                }
                R.id.completeOrderFragment -> {
                    showToolbar()
                    mBinding.showRedirection = true
                    mBinding.title = "Job No. EY10333"
                }
                R.id.financeFragment -> {
                    showToolbar()
                    mBinding.showRedirection = true
                    mBinding.title = "Finance"
                }
                R.id.finishedOrderFragment -> {
                    mBinding.cvToolbar.visibility = GONE
                    mBinding.fabSupport.visibility = GONE
                }
                R.id.companyDetailsFragment -> {
                    showToolbar()
                    mBinding.showRedirection = true
                    mBinding.title = "Company Details"
                }
                R.id.summaryFragment -> {
                    showToolbar()
                    mBinding.showRedirection = true
                    mBinding.title = "Raju's Electrical Service"
                }
                else -> {
                    showToolbar()
                    mBinding.showRedirection = true
                }
            }
        }
    }

    private fun showToolbar() {
        mBinding.cvToolbar.visibility = VISIBLE
        mBinding.fabSupport.visibility = VISIBLE
    }

    private fun setupBodyMenu() {
        val menuItems = arrayOf(
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_dashboard),
                "Dashboard",
                R.id.dashboardFragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_customize_website),
                "Customize Website",
                R.id.customizeWebsiteFragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_order_management),
                "Order Management",
                R.id.orders_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_rupee),
                "Finance",
                R.id.financeFragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_customer),
                "Customers",
                null
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_company_details),
                "Company Details",
                R.id.companyDetailsFragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_summary),
                "Summary",
                R.id.summaryFragment
            ),
        )
        mBinding.rvBodyMenu.adapter =
            DrawerMenuItemAdapter(R.layout.layout_drawer_menu_item).apply {
                setNewItems(menuItems.toList())
                clickHandler = this@DashboardActivity
            }
    }

    private fun setupFooterMenu() {
        val menuItems = arrayOf(
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_settings),
                "Settings",
                null
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_my_account),
                "My Account",
                null
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_customer_support),
                "Customer Support",
                null
            ),
        )
        mBinding.rvFooterMenu.adapter =
            DrawerMenuItemAdapter(R.layout.layout_drawer_menu_item).apply {
                setNewItems(menuItems.toList())
                clickHandler = this@DashboardActivity
            }
    }

    override fun openMenu(view: View) {
        view.disableMultiTap()
        mBinding.dlMenu.openDrawer(Gravity.LEFT)
    }

    override fun closeMenu(view: View) {
        view.disableMultiTap()
        mBinding.dlMenu.closeDrawer(Gravity.LEFT)
    }

    override fun redirect(id: Int?) {
        mBinding.dlMenu.closeDrawer(Gravity.LEFT)
        id?.let {
            if (it == R.id.customizeWebsiteFragment) {
                navController.navigate(
                    R.id.dashboardFragment, bundleOf(
                        getString(R.string.key_updated_dashboard) to true
                    )
                )
            } else
                navController.navigate(id)
        }
    }

    override fun openWebsite(view: View) {
        view.disableMultiTap()
        navController.navigate(R.id.customizeWebsiteFragment)
    }

    override fun onBackPressed() {
        if (mBinding.dlMenu.isOpen)
            mBinding.dlMenu.closeDrawer(Gravity.LEFT)
        else if (!navController.navigateUp())
            super.onBackPressed()
    }

    fun changeHeaderForDashboard() {
        mBinding.title = "Raju's Electrical Service"
        mBinding.showRedirection = true
        mBinding.tvTitle.setTextColor(resources.getColor(R.color.input_field_value))
    }

}