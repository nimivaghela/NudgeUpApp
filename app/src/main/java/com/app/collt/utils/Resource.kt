package com.app.collt.utils

import com.app.collt.domain.models.response.ApiError
import com.google.gson.annotations.SerializedName

data class Resource<T>(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?,
    var error: ApiError? = null


    ) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String?, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message, null)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}




