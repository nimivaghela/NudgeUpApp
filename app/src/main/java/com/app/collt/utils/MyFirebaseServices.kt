package com.app.collt.utils

import android.util.Log
import com.app.collt.domain.models.UserHolder
import com.app.collt.helper.THIS_TAG
import com.google.android.gms.tasks.Task
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import org.koin.android.ext.android.inject
import timber.log.Timber


class MyFirebaseServices : FirebaseMessagingService() {
    val mUserHolder: UserHolder by inject()


    override fun onNewToken(token: String) {
        mUserHolder.setFCMToken(token)
        super.onNewToken(token)
        Timber.d("onNewToken: $token")
    }
}