package com.provider.unobridge.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.WebsiteViewFragmentBinding
import com.provider.unobridge.room.entities.ProfileData
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class WebsiteViewFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: WebsiteViewFragmentBinding
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
            mBinding = WebsiteViewFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }


        setUpUI()

        return mBinding.root
    }

    private fun setUpUI() {

    }


    inner class ClickHandler {


        fun onClickBack() {
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