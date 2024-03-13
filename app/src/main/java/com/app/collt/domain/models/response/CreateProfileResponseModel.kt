package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class CreateProfileResponseModel(
    var id: String? = "",

    var error: ApiError? = null,

    @SerializedName("message")
    var message: String = "",

//    @SerializedName("data")
//    var data: LoginModel? = null


)
