package com.provider.unobridge.ui.fragments.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.provider.unobridge.repo.RemoteRepository

class LoginViewModelFactory(
    private val authenticationRepository: RemoteRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(authenticationRepository) as T
    }
}