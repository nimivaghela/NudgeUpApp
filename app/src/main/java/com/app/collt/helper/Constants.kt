package com.app.collt.helper

import com.app.collt.BuildConfig

const val THIS_TAG = "THIS_TAG"
const val TERMS_AND_CONDITIONS = "TERMS_AND_CONDITIONS"
const val PRIVACY_POLICY = "PRIVACY_POLICY"
const val TROUBLE_SIGNING_UP = "TROUBLE_SIGNING_UP"
const val IS_FROM_LOGIN = "IS_FROM_LOGIN"
const val IS_FROM_SIGN_UP = "IS_FROM_SIGN_UP"
const val BOARDING_POS = "BOARDING_POS"
const val IS_FROM_SYNC_CONTACT = "IS_FROM_SYNC_CONTACT"
const val IS_FROM_CONTACT_LIST = "IS_FROM_CONTACT_LIST"
const val IS_FROM_WALKTHROUGH = "IS_FROM_WALKTHROUGH"
const val IS_FROM_PROFILE_PIC = "IS_FROM_PROFILE_PIC"
const val IS_FROM_CREATE_PROFILE = "IS_FROM_CREATE_PROFILE"
const val IS_FROM_CHOOSE_USERNAME = "IS_FROM_CHOOSE_USERNAME"
const val IS_FROM_HOME = "IS_FROM_HOME"
const val ERROR = "ERROR"
const val BITMAP_IMAGE = "BITMAP_IMAGE"
const val IMAGE_URI = "IMAGE_URI"
const val AUTHORIZATION = "AUTHORIZATION"

const val authToken = "authToken"

const val FRAGMENT_HOME = "FRAGMENT_HOME"
const val FRAGMENT_SEARCH = "FRAGMENT_SEARCH"
const val FRAGMENT_NOTIFICATION = "FRAGMENT_NOTIFICATION"
const val FRAGMENT_SETTING = "FRAGMENT_SETTING"

const val PHONE_NO = "PHONE_NO"

const val ANDROID = "ANDROID"
const val CROP_IMAGE_DATA = "CROP_IMAGE_DATA"

const val TroubleLoginUrl = BuildConfig.API_URL + "page/trouble-signing.html"
const val PrivacyPolicyUrl = BuildConfig.API_URL + "page/privacy-policy.html"
const val Terms_ConditionUrl = BuildConfig.API_URL + "page/terms-conditions.html"
const val CONTACT_US = BuildConfig.API_URL + "page/contact-us.html"
const val COMMUNITY_GUIDELINES = BuildConfig.API_URL + "page/community-guidelines.html"
const val FAQ = BuildConfig.API_URL + "page/frequently-asked-questions.html"


object StatusCode {
    const val STATUS_1 = 1
    const val STATUS_0 = 0
    const val STATUS_404 = 404
    const val STATUS_401 = 401
    const val STATUS_412 = 412
    const val STATUS_403 = 403
    const val STATUS_500 = 500
    const val STATUS_422 = 422
    const val STATUS_409 = 409
    const val STATUS_426 = 426
}
