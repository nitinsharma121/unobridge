package com.provider.unobridge.repo

import com.provider.unobridge.data.LoginParams
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.BaseResponse


interface RemoteRepository {

    fun login(
        registerRequest: LoginParams,
        onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit
    )

    fun addProfile(
        registerRequest: ProfileParams,
        onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit
    )

    fun getProfile(
        onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit
    )

    fun getStates(
        onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit
    )


}