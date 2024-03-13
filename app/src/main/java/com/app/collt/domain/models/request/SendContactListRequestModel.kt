package com.app.collt.domain.models.request

import com.app.collt.domain.models.ContactModel
import com.google.gson.annotations.SerializedName

data class SendContactListRequestModel(
    @SerializedName("contacts")
    var contacts: ArrayList<ContactModel> = arrayListOf()
)

/*data class ContactModel(
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("contactNumber")
    var contactNumber: String? = "",
    @SerializedName("phoneNumber")
    var phoneNumber: String? = "",
    @SerializedName("firstName")
    var firstName: String? = "",
    @SerializedName("lastName")
    var lastName: String? = ""

    )*/
