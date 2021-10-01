package com.provider.unobridge.dataSources.remote

import com.provider.unobridge.network.core.APIService
import com.provider.unobridge.R
import com.provider.unobridge.base.MainApplication
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.data.LoginParams
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.BaseResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class RemoteDataSourceImp(
    private val apiService: APIService
) : RemoteDataSource {


    override suspend fun login(payload: LoginParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.login(payload)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.message = APIService.getErrorMessageFromGenericResponse(e).toString()
            response.error = 1
        }
        return response
    }


    override suspend fun addProfile(payload: ProfileParams): BaseResponse {
        var response = BaseResponse()

        try {
            var params = HashMap<String, RequestBody>()
            params["id"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), Prefs.init().userId!!)
            params["deviceType"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), payload.deviceType!!)
            params["deviceToken"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), payload.deviceToken!!)
            params["userType"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), payload.userType!!)
            params["name"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), payload.name!!)
            params["email"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), payload.email!!)
            params["aadharNumber"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), payload.aadharNumber!!)


            if(payload.userType==MainApplication.instance.getString(R.string.driver)){
                params["licenseNo"] =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), payload.licenseNo!!)
                response = apiService.addProfile(params, payload.profileImage, payload.aadharFrontImage,payload.aadharBackImage,payload.licenseFrontImage,payload.licenseBackImage)

            }else{
                response = apiService.addProfile(params, payload.profileImage, payload.aadharBackImage,payload.aadharBackImage,null,null)

            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.message = APIService.getErrorMessageFromGenericResponse(e).toString()
            response.error = 1
        }
        return response
    }

    override suspend fun getProfile(): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.getProfile()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.message = APIService.getErrorMessageFromGenericResponse(e).toString()
            response.error = 1
        }
        return response
    }

    override suspend fun getStates(): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.getStatesList()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.message = APIService.getErrorMessageFromGenericResponse(e).toString()
            response.error = 1
        }
        return response
    }


}