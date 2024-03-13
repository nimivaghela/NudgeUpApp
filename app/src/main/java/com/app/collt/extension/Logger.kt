package com.app.collt.extension

import android.text.TextUtils
import android.util.Log
import androidx.multidex.BuildConfig


fun Any.logError(tag: String = this.javaClass.simpleName, msg: String?) {
    msg?.run {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(tag)) Log.e(this.javaClass.simpleName, msg)
            else Log.e(tag, msg)
        }
    }
}

fun Any.logDebug(tag: String = this.javaClass.simpleName, msg: String?) {
    msg?.run {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(tag)) Log.d(this.javaClass.simpleName, msg)
            else Log.d(tag, msg)
        }
    }
}

fun Any.logVerbose(tag: String = this.javaClass.simpleName, msg: String?) {
    msg?.run {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(tag)) Log.v(this.javaClass.simpleName, msg)
            else Log.v(tag, msg)
        }
    }
}

fun Any.logInfo(tag: String = this.javaClass.simpleName, msg: String?) {
    msg?.run {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(tag)) Log.i(this.javaClass.simpleName, msg)
            else Log.i(tag, msg)
        }
    }
}