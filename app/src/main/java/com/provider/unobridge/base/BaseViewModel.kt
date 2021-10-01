package com.provider.unobridge.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    var showMessage = MutableLiveData("")
    var showLoading = MutableLiveData(false)
    var showNoDataMessage = MutableLiveData(false)
    var toastMessage = MutableLiveData<String>()
    var link: String = ""

}