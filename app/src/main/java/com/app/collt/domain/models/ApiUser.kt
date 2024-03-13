package com.app.collt.domain.models

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("slogan")
    val slogan: String = "",
    @SerializedName("head_quaters")
    val head_quaters: String = "",
    @SerializedName("website")
    val website: String = ""

)