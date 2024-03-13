package com.app.collt.utils

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class ApiObserver<M> : Observer<M> {
    abstract fun onSuccess(data: M)

    abstract fun onFailure(code: Int, errorMessage: String)

    override fun onComplete() {
    }

    override fun onNext(t: M) {
        onSuccess(t)
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {

                var baseDao: M? = null
                try {
                    val body = e.response()?.errorBody()
                } catch (exception: Exception) {
                    onFailure(504, exception.message!!)
                }

                onFailure(504, "Error Response")
            }

            is UnknownHostException -> onFailure(
                -1,
                "An error has occurred while connecting to the server: ${e.message}"
            )

            is SocketTimeoutException -> onFailure(
                -1,
                "An error has occurred while connecting to the server: ${e.message}"
            )

            else -> onFailure(-1, e.message ?: "Unknown error occurred")
        }

    }
}