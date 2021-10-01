package com.provider.unobridge.ui.fragments.account.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.provider.unobridge.repo.RemoteRepository

class AccountViewModelFactory(
    private val authenticationRepository: RemoteRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(authenticationRepository) as T
    }
}