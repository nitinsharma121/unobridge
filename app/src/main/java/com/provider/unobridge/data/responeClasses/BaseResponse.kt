package com.provider.unobridge.data.responeClasses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BaseResponse(
    @field:SerializedName("data")
    var data: Data? = null,

    @field:SerializedName("error")
    var error: Int? = 0,

    @field:SerializedName("message")
    var message: String? = "",

    @field:SerializedName("status")
    val status: String? = ""
) : Parcelable

@Parcelize
data class Data(
    @field:SerializedName("loginData")
    var loginData: ResponseLogin? = ResponseLogin(),
    @field:SerializedName("profileData")
    var profileData: ResponseProfileData? = ResponseProfileData(),
    @field:SerializedName("stateData")
    val responseStates: List<ResponseStatesItem> = ArrayList()

) : Parcelable


@Parcelize
data class ResponseStatesItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("fullImageUrl")
    val fullImageUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Parcelable


@Parcelize
data class ResponseLogin(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("mobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("isProfileCompleted")
    val isProfileCompleted: Int? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable


@Parcelize
data class ResponseProfileData(

    @field:SerializedName("userAadharDetails")
    val userAadharDetails: UserAadharDetails? = null,

    @field:SerializedName("userDriverLicenseDetails")
    val userDriverLicenseDetails: UserDriverLicenseDetails? = null,

    @field:SerializedName("userProfiles")
    val userProfiles: UserProfiles? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("mobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("userType")
    val userType: String? = null
) : Parcelable

@Parcelize
data class UserProfiles(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("profileImage")
    val profileImage: String? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable

@Parcelize
data class UserDriverLicenseDetails(

    @field:SerializedName("licenseNo")
    val licenseNo: String? = null,

    @field:SerializedName("licenseBackImage")
    val licenseBackImage: String? = null,

    @field:SerializedName("licenseFrontImage")
    val licenseFrontImage: String? = null
) : Parcelable

@Parcelize
data class UserAadharDetails(

    @field:SerializedName("aadharFrontImage")
    val aadharFrontImage: String? = null,

    @field:SerializedName("aadharNumber")
    val aadharNumber: String? = null,

    @field:SerializedName("aadharBackImage")
    val aadharBackImage: String? = null
) : Parcelable
