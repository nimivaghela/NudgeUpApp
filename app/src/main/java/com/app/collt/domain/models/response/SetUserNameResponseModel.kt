package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class SetUserNameResponseModel(
    @SerializedName("message")
    var message: String? = null,
    var data: String? = null,
    var error: ApiError? = null
)
