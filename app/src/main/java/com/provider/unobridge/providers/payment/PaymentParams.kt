package com.provider.unobridge.providers.payment

import com.google.gson.annotations.SerializedName

data class PaymentParams(
    @field:SerializedName("name")
    var name: String? = null,
    @field:SerializedName("description")
    var description: String? = null,
    @field:SerializedName("image")
    var image: String? = null,
    @field:SerializedName("themeColor")
    var themeColor: String? = null,
    @field:SerializedName("currency")
    var currency: String? = null,
    @field:SerializedName("order_id")
    var order_id: String? = null,
    @field:SerializedName("amount")
    var amount: String? = null,
    @field:SerializedName("send_sms_hash")
    var send_sms_hash: Boolean? = true,
    @field:SerializedName("prefill")
    var prefill: ContactDetails = ContactDetails(),
)

data class ContactDetails(
    @field:SerializedName("email")
    var email: String? = null,
    @field:SerializedName("contact")
    var contact: String? = null
)
