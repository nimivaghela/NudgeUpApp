package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class CheckUserNameAvailableResponseModel(
    @SerializedName("message")
    var message: String = "",

    )
