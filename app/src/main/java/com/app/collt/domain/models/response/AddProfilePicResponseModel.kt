package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class AddProfilePicResponseModel(
    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: AddProfileModel? = null
)

data class AddProfileModel(
    @SerializedName("media")
    var media: String = "",
    @SerializedName("thumb")
    var thumb: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("userId")
    var userId: String = "",
    @SerializedName("index")
    var index: String = "",
    @SerializedName("mediaType")
    var mediaType: String = "",
    @SerializedName("isProfilePic")
    var isProfilePic: String = "",
    @SerializedName("ratio")
    var ratio: String = "",
    @SerializedName("updatedAt")
    var iupdatedAtndex: String = "",
    @SerializedName("createdAt")
    var createdAt: String = "",


    )
