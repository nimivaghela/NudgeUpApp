package com.app.collt.domain.models.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class PhoneExistResponseModel {
    @SerializedName("data")
    var data: JsonObject? = null

    @SerializedName("message")
    var message: String? = null

    var error: ApiError? = null
}