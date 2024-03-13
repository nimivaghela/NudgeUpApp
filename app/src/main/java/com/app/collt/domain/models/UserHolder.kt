package com.app.collt.domain.models

import android.content.SharedPreferences
import com.app.collt.extension.prefBoolean
import com.app.collt.extension.prefInt
import com.app.collt.extension.prefString


class UserHolder(preference: SharedPreferences) {
    var authToken by preference.prefString("")
    var name by preference.prefString("")
    var userName by preference.prefString("")
    var phone by preference.prefString("")
    var fcmToken by preference.prefString("")
    var birthdate by preference.prefString("")
    var image by preference.prefString("")
    var isLogin by preference.prefBoolean(false)


    fun setUserData(
        name: String?,
        phone: String?,
        authToken: String?,
        birthdate: String?,
        userName: String?,
        image: String?,

        ) {
        this.name = name
        this.phone = phone
        this.authToken = authToken
        this.birthdate = birthdate
        this.userName = userName
        this.image = image
    }

    /*    fun setVerificationStatus(isVerified: Boolean) {
            this.isEmailVerified = isVerified
        }

        fun setMobileVerificationStatus(isVerified: Boolean) {
            this.isMobileVerified = isVerified
        }

        fun setDeviceToken(token: String) {
            this.notificationToken = token
        }

        fun setEmail(email: String) {
            this.mEmail = email
        }

        fun updateOnBoarding(completedOnbordingScreen: Int) {
            this.mCompleteOnBoardingScreenNum = completedOnbordingScreen
        }*/

    fun setLoginStatus(isLogin: Boolean) {
        this.isLogin = isLogin
    }

    /*fun setAfterLoginWhereToNavigate(isFromWhichScreen: String) {
        this.isFromWhichScreen = isFromWhichScreen
    }*/

    fun setFCMToken(fcmToken: String) {
        this.fcmToken = fcmToken
    }

    /*   fun setAuthToken(loginAuthToken: String) {
           this.loginAuthToken = loginAuthToken
       }

       fun setName(name: String) {
           this.name = name
       }

       fun setBirthDate(loginAuthToken: String) {
           this.loginAuthToken = loginAuthToken
       }*/


    fun clear() {
        this.name = ""
        this.phone = ""
        this.authToken = ""
        this.birthdate = ""
        this.userName = ""
        this.image = ""
    }
}