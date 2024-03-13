package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class GetUserContactListResponseModel(
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: GetUserData? = null,
    var error: ApiError? = null
)

data class GetUserData(
    @SerializedName("users") var users: ArrayList<GetSyncUserData> = arrayListOf(),
    @SerializedName("contacts") var contacts: ArrayList<GetSyncUserData> = arrayListOf(),

    )

data class GetSyncUserData(
    @SerializedName("image") var image: String = "",
    @SerializedName("meFollowing") var meFollowing: Int = 0,
    @SerializedName("userFollowing") var userFollowing: Int = 0,
    @SerializedName("meBlocked") var meBlocked: Int = 0,
    @SerializedName("userBlocked") var userBlocked: Int = 0,
    @SerializedName("id") var id: String = "",
    @SerializedName("userName") var userName: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("website") var website: String = "",
    @SerializedName("pauseNotification") var pauseNotification: Boolean = false,
    @SerializedName("isActive") var isActive: Boolean = false,
    @SerializedName("age") var age: Int = 0,
    @SerializedName("createdAt") var createdAt: String = "",
    @SerializedName("updatedAt") var updatedAt: String = "",
    @SerializedName("userCategory") var userCategory: UserCategory? = null,
    @SerializedName("userMedia") var userMedia: ArrayList<UserMedia>? = arrayListOf(),


    )

data class UserCategory(
    @SerializedName("id") var id: String = "",
    @SerializedName("status") var status: String = "",
)
