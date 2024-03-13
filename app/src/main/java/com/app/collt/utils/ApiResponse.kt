package com.app.collt.utils

interface ApiResponse {
    interface DataResponse<T> {
        fun onShowProgress()
        fun onHideProgress()
        fun onEmpty()
        fun onSuccess(data: T)
        fun onFailed(statusCode: Int, errorMessage: String? = "")
    }
}