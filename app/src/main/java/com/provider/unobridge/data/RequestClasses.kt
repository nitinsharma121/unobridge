package com.provider.unobridge.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.provider.unobridge.base.Prefs
import kotlinx.android.parcel.Parcelize
import okhttp3.MultipartBody
import java.util.stream.IntStream

data class StateData(val image: Int, val stateName: String)
data class SitesData(val title: String, val url: String)
data class RideMessage(val message: String)


data class LoginParams(
    @SerializedName("mobileNo")
    var mobileNo: String? = null,
    @SerializedName("deviceType")
    var deviceType: String? = "Android",
    @SerializedName("deviceToken")
    var deviceToken: String? = Prefs.init().deviceToken,
)

data class SubscriptionParams(

    @SerializedName("transactionId")
    var transactionId: String? = null,

    @SerializedName("amount")
    var amount: String? = null,

    @SerializedName("purchasedAt")
    var purchasedAt: String? = null,

    @SerializedName("isActive")
    var isActive: Int? = null

)

@Parcelize
class ProfileParams : Parcelable {
    var id: String? = null
    var deviceType: String? = "Android"
    var deviceToken: String? = Prefs.init().deviceToken
    var userType: String? = null
    var profileImage: MultipartBody.Part? = null
    var name: String? = null
    var email: String? = null
    var aadharNumber: String? = null
    var aadharFrontImage: MultipartBody.Part? = null
    var aadharBackImage: MultipartBody.Part? = null
    var licenseNo: String? = null
    var licenseFrontImage: MultipartBody.Part? = null
    var licenseBackImage: MultipartBody.Part? = null
}
