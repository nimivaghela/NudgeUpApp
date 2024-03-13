package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

class SignUpResponseModel {
    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: SignUpModel? = null
    var error: ApiError? = null
}

data class SignUpModel(
    @SerializedName("image")
    var image: String = "",
    @SerializedName("thumb")
    var thumb: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("bio")
    var bio: String = "",
    @SerializedName("followerCount")
    var followerCount: String = "",
    @SerializedName("followingCount")
    var followingCount: String = "",
    @SerializedName("followedHashTagCount")
    var followedHashTagCount: String = "",
    @SerializedName("reportCount")
    var reportCount: String = "",
    @SerializedName("isRecommended")
    var isRecommended: String = "",
    @SerializedName("pauseNotification")
    var pauseNotification: String = "",
    @SerializedName("isEmailVerified")
    var isEmailVerified: String = "",
    @SerializedName("isPhoneVerified")
    var isPhoneVerified: String = "",
    @SerializedName("isActive")
    var isActive: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("countryCode")
    var countryCode: String = "",
    @SerializedName("updatedAt")
    var updatedAt: String = "",
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("authToken")
    var authToken: String = ""
)