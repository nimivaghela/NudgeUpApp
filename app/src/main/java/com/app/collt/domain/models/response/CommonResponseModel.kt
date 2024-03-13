package com.app.collt.domain.models.response

import com.app.collt.helper.ERROR
import com.app.collt.utils.Resource
import com.google.gson.annotations.SerializedName

data class CommonResponseModel<T>(
    @SerializedName("status")
    var status: Int,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("type")
    var type: String? = ERROR,
    @SerializedName("data")
    var data: T? = null
)


data class ErrorModel(
    @SerializedName("status")
    val errorCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("type")
    val type: String = ERROR
)

data class RequestState<T>(
    var error: ApiError? = null,
    var progress: Boolean = false,
    var apiResponse: CommonResponseModel<T>? = null
)

data class ApiError(
    val errorCode: Int?,
    val errorState: String,
    val customMessage: String?,
    val type: String
)

