package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class AvailableUserNameListResponseModel(
    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: ArrayList<String> = arrayListOf()


)
