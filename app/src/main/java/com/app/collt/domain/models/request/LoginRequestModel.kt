package com.app.collt.domain.models.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginRequestModel(
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("token")
    var token: String? = null,
    @SerializedName("deviceType")
    var deviceType: String? = null,
    @SerializedName("deviceToken")
    var deviceToken: String? = null,
    @SerializedName("appVersion")
    var appVersion: String? = null,
    @SerializedName("timeZone")
    var timeZone: String? = null
) : Serializable