package com.app.collt.domain.models.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class PhoneAvailableResponseModel {
    @SerializedName("data")
    var data: PhoneAvailableDataModel? = null

    @SerializedName("message")
    var message: String? = null

    var error: ApiError? = null

}

data class PhoneAvailableDataModel(
    var id: String = ""
)