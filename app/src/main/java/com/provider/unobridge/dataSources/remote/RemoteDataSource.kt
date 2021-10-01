package com.provider.unobridge.dataSources.remote

import com.provider.unobridge.data.LoginParams
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.BaseResponse

interface RemoteDataSource {


    suspend fun login(payload: LoginParams): BaseResponse
    suspend fun addProfile(payload: ProfileParams): BaseResponse
    suspend fun getProfile(): BaseResponse
    suspend fun getStates(): BaseResponse


}