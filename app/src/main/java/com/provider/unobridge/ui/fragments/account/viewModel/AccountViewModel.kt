package com.provider.unobridge.ui.fragments.account.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseViewModel
import com.provider.unobridge.data.responeClasses.BaseResponse
import com.provider.unobridge.repo.RemoteRepository
import com.provider.unobridge.ui.fragments.home.StatesAdapter
import kotlinx.coroutines.launch

class AccountViewModel(
    private val remoteRepository: RemoteRepository
) :
    BaseViewModel() {

    var data = MutableLiveData<BaseResponse>()
    var statesList = MutableLiveData<BaseResponse>()


    fun getProfile() {
        viewModelScope.launch {
            remoteRepository.getProfile()
            { isSuccess: Boolean, response: BaseResponse ->
                if (isSuccess) {
                    data.postValue(response)

                } else {
                    showMessage.postValue(response.message)
                }
            }
        }
    }

    fun getStatesList() {
        viewModelScope.launch {
            remoteRepository.getStates()
            { isSuccess: Boolean, response: BaseResponse ->
                if (isSuccess) {
                    statesList.postValue(response)

                } else {
                    showMessage.postValue(response.message)
                }
            }
        }
    }


}