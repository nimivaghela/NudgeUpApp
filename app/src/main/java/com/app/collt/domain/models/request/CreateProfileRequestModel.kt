package com.app.collt.domain.models.request

import com.google.gson.annotations.SerializedName

data class CreateProfileRequestModel(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("birthDate")
    var birthDate: String = ""

    )
