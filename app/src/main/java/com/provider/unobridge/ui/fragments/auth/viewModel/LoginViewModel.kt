package com.provider.unobridge.ui.fragments.auth.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.provider.unobridge.base.BaseViewModel
import com.provider.unobridge.data.LoginParams
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.BaseResponse
import com.provider.unobridge.repo.RemoteRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val remoteRepository: RemoteRepository
) :
    BaseViewModel() {


    var loginParams = LoginParams()
    var profileParams = ProfileParams()
    var data = MutableLiveData<BaseResponse>()


    fun socialLogin() {
        viewModelScope.launch {
            remoteRepository.login(loginParams)
            { isSuccess: Boolean, response: BaseResponse ->
                showLoading.postValue(false)
                if (isSuccess) {
                    data.postValue(response)

                } else {
                    showMessage.postValue(response.message)
                }
            }
        }
    }

    fun addProfile() {
        showLoading.postValue(true)
        viewModelScope.launch {
            remoteRepository.addProfile(profileParams)
            { isSuccess: Boolean, response: BaseResponse ->
                showLoading.postValue(false)
                if (isSuccess) {
                    data.postValue(response)

                } else {
                    showMessage.postValue(response.message)
                }
            }
        }
    }


}