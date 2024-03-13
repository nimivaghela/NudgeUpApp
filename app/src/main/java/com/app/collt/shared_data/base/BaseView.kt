package com.app.collt.shared_data.base

interface BaseView {
    fun internalServer()
    fun onUnknownError(error: String?)
    fun onTimeout()
    fun onNetworkError()
    fun onConnectionError()
    fun generalErrorAction()
    fun onServerDown()
    fun forbidden(error: String?)
    fun autoLogout()
    fun loginFirst()
    fun onOTPExpire(error: String?)
    fun onSubscribeRequired(error: String?)
    fun onVerificationError(): Unit?
}