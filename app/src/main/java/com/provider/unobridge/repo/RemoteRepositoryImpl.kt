package com.provider.unobridge.repo

import com.provider.unobridge.data.LoginParams
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.BaseResponse
import com.provider.unobridge.dataSources.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RemoteRepositoryImpl(
    private val authenticationDataSource: RemoteDataSource
) :
    RemoteRepository {


    override fun login(
        registerRequest: LoginParams,
        onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = authenticationDataSource.login(registerRequest)

            if (response.error == 1) {
                onResult(false, response)
            } else {
                onResult(true, response)
            }
        }
    }


    override fun addProfile(
        registerRequest: ProfileParams,
        onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = authenticationDataSource.addProfile(registerRequest)
            if (response.error == 1) {
                onResult(false, response)
            } else {
                onResult(true, response)
            }
        }
    }

    override fun getProfile(onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = authenticationDataSource.getProfile()
            if (response.error == 1) {
                onResult(false, response)
            } else {
                onResult(true, response)
            }
        }
    }

    override fun getStates(onResult: (isSuccess: Boolean, baseResponse: BaseResponse) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = authenticationDataSource.getStates()
            if (response.error == 1) {
                onResult(false, response)
            } else {
                onResult(true, response)
            }
        }
    }


}