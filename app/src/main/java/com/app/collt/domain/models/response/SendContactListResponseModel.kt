package com.app.collt.domain.models.response

import com.google.gson.annotations.SerializedName

data class SendContactListResponseModel(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("data")
    var data: ContactDataModel? = null,
    var error: ApiError? = null

)

data class ContactDataModel(
    @SerializedName("users")
    var users: ArrayList<UserDataModel>? = null,
    @SerializedName("contacts")
    var contacts: ArrayList<UserDataModel>? = null,
)

data class UserDataModel(
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("userId")
    var userId: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("contactNumber")
    var contactNumber: String? = "",
    @SerializedName("phoneNumber")
    var phoneNumber: String? = "",
    @SerializedName("firstName")
    var firstName: String? = "",
    @SerializedName("lastName")
    var lastName: String? = "",
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("contactUserId")
    var contactUserId: String? = "",


    )
