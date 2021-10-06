package com.provider.unobridge.ui.activities.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.dashboardFragment -> {
                    mBinding.title = "Welcome, Sam Fisher!"
                    mBinding.showRedirection = false
                    mBinding.tvTitle.setTextColor(resources.getColor(R.color.action_button_color))
                }else ->{
                    mBinding.showRedirection = true
                }
            }
        }
    }

    private fun setupBodyMenu() {
        val menuItems = arrayOf(
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_dashboard),
                "Dashboard",
                R.id.home_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_customize_website),
                "Customize Website",
                R.id.home_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_order_management),
                "Order Management",
                R.id.orders_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_finance),
                "Finance",
                R.id.financeFragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_customer),
                "Customers",
                R.id.home_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_company_details),
                "Company Details",
                R.id.home_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_summary),
                "Summary",
                R.id.home_fragment
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
                R.id.home_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_my_account),
                "My Account",
                R.id.home_fragment
            ),
            DrawerMenuItem(
                resources.getDrawable(R.drawable.ic_customer_support),
                "Customer Support",
                R.id.home_fragment
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

    override fun redirect(id: Int) {
        navController.navigate(id)
    }

}