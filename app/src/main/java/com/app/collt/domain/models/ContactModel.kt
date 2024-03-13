package com.app.collt.domain.models

import com.google.gson.annotations.SerializedName

class ContactModel {
//    var name: String? = null
//    var number: String? = null

    @SerializedName("email")
    var email: String? = "no email"

    @SerializedName("contactNumber")
    var contactNumber: String? = ""

    @SerializedName("phoneNumber")
    var phoneNumber: String? = ""

    @SerializedName("firstName")
    var firstName: String? = ""

    @SerializedName("lastName")
    var lastName: String? = ""
}