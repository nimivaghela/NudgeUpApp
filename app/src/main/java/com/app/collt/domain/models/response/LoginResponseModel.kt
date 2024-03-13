package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

class LoginResponseModel {
    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: LoginModel? = null
    var error: ApiError? = null
}

data class LoginModel(
    @SerializedName("image")
    var image: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("userName")
    var userName: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("countryCode")
    var countryCode: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("bio")
    var bio: String? = "",
    @SerializedName("birthDate")
    var birthDate: String? = "",
    @SerializedName("age")
    var age: String? = "",
    @SerializedName("followerCount")
    var followerCount: Int? = 0,
    @SerializedName("followingCount")
    var followingCount: Int? = 0,
    @SerializedName("followedHashTagCount")
    var followedHashTagCount: Int? = 0,
    @SerializedName("isRecommended")
    var isRecommended: Boolean? = false,
    @SerializedName("isEmailVerified")
    var isEmailVerified: Boolean? = false,
    @SerializedName("isPhoneVerified")
    var isPhoneVerified: Boolean? = false,
    @SerializedName("isActive")
    var isActive: Boolean? = false,
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("updatedAt")
    var updatedAt: String? = "",
    @SerializedName("reportCount")
    var reportCount: Int? = 0,
    @SerializedName("pauseNotification")
    var pauseNotification: Boolean? = false,
    @SerializedName("website")
    var website: String? = "",
    @SerializedName("lastLoginAt")
    var lastLoginAt: String? = "",
    @SerializedName("publicColtCount")
    var publicColtCount: Int? = 0,
    @SerializedName("privateColtCount")
    var privateColtCount: Int? = 0,
    @SerializedName("coltGroupCount")
    var coltGroupCount: Int? = 0,
    @SerializedName("userCategory")
    var userCategory: UserCategory? = null,
    @SerializedName("userMedia")
    var userMedia: ArrayList<UserMedia?>? = arrayListOf(),
    @SerializedName("authToken")
    var authToken: String? = ""
)

data class UserMedia(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("media")
    var media: String = "",
    @SerializedName("thumb")
    var thumb: String = "",
    @SerializedName("index")
    var index: String = "",
    @SerializedName("isProfilePic")
    var isProfilePic: Boolean = false,
    @SerializedName("ratio")
    var ratio: String = "",
    @SerializedName("mediaType")
    var mediaType: String = "",
    @SerializedName("createdAt")
    var medcreatedAtiaType: String = ""

)