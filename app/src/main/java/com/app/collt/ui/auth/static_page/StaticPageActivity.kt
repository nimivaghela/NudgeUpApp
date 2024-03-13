package com.app.collt.ui.auth.static_page

import android.annotation.SuppressLint
import com.app.collt.R
import com.app.collt.databinding.ActivityStaticPageBinding
import com.app.collt.extension.showProgress
import com.app.collt.helper.PRIVACY_POLICY
import com.app.collt.helper.PrivacyPolicyUrl
import com.app.collt.helper.TERMS_AND_CONDITIONS
import com.app.collt.helper.TROUBLE_SIGNING_UP
import com.app.collt.helper.Terms_ConditionUrl
import com.app.collt.helper.TroubleLoginUrl
import com.app.collt.shared_data.base.BaseActivity

class StaticPageActivity : BaseActivity() {
    override val binding: ActivityStaticPageBinding by binding(R.layout.activity_static_page)

    private var isFromTermsAndCondition: Boolean = false
    private var isFromPrivacyPolicy: Boolean = false
    private var isFromTroubleSigningUp: Boolean = false

    override fun handleListener() {

    }

    override fun initObserver() {

    }

    override fun initView() {
        dialog = showProgress(this)

        isFromTermsAndCondition = intent.getBooleanExtra(TERMS_AND_CONDITIONS, false)
        isFromPrivacyPolicy = intent.getBooleanExtra(PRIVACY_POLICY, false)
        isFromTroubleSigningUp = intent.getBooleanExtra(TROUBLE_SIGNING_UP, false)

        binding.webView.setBackgroundColor(getColor(R.color.colorPrimary))

        if (isFromTermsAndCondition) {
            setUrlToWebView(Terms_ConditionUrl)

            setToolBar(
                binding.mToolbar,
                isBackEnable = true,
                isTitleEnable = true,
                isTitleTroubleEnable = false,
                titleText = getString(R.string.terms_and_conditions),
                titleTrouble = "",
                isSkipVisible = false,
                titleSkipText = "",
                isFriendsVisible = false,
                isLogoVisible = false,
                isAddGroup = false
            )
        }
        if (isFromPrivacyPolicy) {
            setUrlToWebView(PrivacyPolicyUrl)
            setToolBar(
                binding.mToolbar,
                isBackEnable = true,
                isTitleEnable = true,
                isTitleTroubleEnable = false,
                titleText = getString(R.string.privacy_policy),
                titleTrouble = "",
                isSkipVisible = false,
                titleSkipText = "",
                isFriendsVisible = false,
                isLogoVisible = false,
                isAddGroup = false
            )
        }
        if (isFromTroubleSigningUp) {
            setUrlToWebView(TroubleLoginUrl)
            setToolBar(
                binding.mToolbar,
                isBackEnable = true,
                isTitleEnable = true,
                isTitleTroubleEnable = false,
                titleText = getString(R.string.trouble_signing_up_with_ques),
                titleTrouble = "",
                isSkipVisible = false,
                titleSkipText = "",
                isFriendsVisible = false,
                isLogoVisible = false,
                isAddGroup = false
            )
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUrlToWebView(url: String) {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
        dialog.dismiss()
    }

}