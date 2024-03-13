package com.app.collt.ui.auth.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityLoginBinding
import com.app.collt.extension.AppUtils
import com.app.collt.extension.hideKeyboard
import com.app.collt.extension.isNetworkAvailable
import com.app.collt.extension.showProgress
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.helper.IS_FROM_LOGIN
import com.app.collt.helper.IS_FROM_SIGN_UP
import com.app.collt.helper.PHONE_NO
import com.app.collt.helper.THIS_TAG
import com.app.collt.helper.TROUBLE_SIGNING_UP
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.otp_screen.OtpVerificationActivity
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.ui.auth.static_page.StaticPageActivity
import com.app.collt.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity() {
    private var isFromLogin: Boolean = false
    private var isFromSignUp: Boolean = false
    private val loginViewModel: LoginViewModel by viewModel()

    override val binding: ActivityLoginBinding by binding(R.layout.activity_login)

    override fun handleListener() {
        binding.mToolbar.tvTroubleSignUp.setOnClickListener {
            val intent = Intent(this, StaticPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(TROUBLE_SIGNING_UP, true)
            startActivityWithAnimation(intent)
        }

        binding.btnContinue.setOnClickListener {
            //                loginViewModel.fetchUsers()
            if (isValid()) {
                dialog = showProgress(this@LoginActivity)
                hideKeyboard()
                if (isNetworkAvailable()) {
                    if (isFromLogin) {
                        loginViewModel.checkExistPhone(
                            binding.etPhoneNo.text.toString()
                        )
                    }
                    if (isFromSignUp) {
                        loginViewModel.checkAvailablePhone(
                            binding.etPhoneNo.text.toString(),
                            this@LoginActivity,
                            mDisposable
                        )
                    }
                }
            }
        }
    }

    private fun isValid(): Boolean {
        binding.apply {
            return if (etPhoneNo.text.toString().isEmpty()) {
                binding.layoutMain.snackBarWithAction(this@LoginActivity,getString(R.string.error_enter_phone_no))
                false
            } else if (etPhoneNo.text.toString().length < 10) {
                binding.layoutMain.snackBarWithAction(this@LoginActivity,getString(R.string.error_enter_vallid_phone_no))
                false
            } else {
                true
            }
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.stateFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        Log.d(THIS_TAG, "initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
//                        binding.layoutMain.snackBarWithAction(resource.data?.error?.customMessage.toString())
                        Log.d(THIS_TAG, "initObserver: ERROR")
                    }

                    Resource.Status.LOADING -> {
                        Log.d(THIS_TAG, "initObserver: LOADING")
                    }
                }
            }
        }

        lifecycleScope.launch {
            loginViewModel.phoneExistResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        dialog.dismiss()
                        val intent = Intent(this@LoginActivity, OtpVerificationActivity::class.java)
                        intent.putExtra(PHONE_NO, binding.etPhoneNo.text.toString())
                        intent.putExtra(IS_FROM_LOGIN, isFromLogin)
                        startActivityWithAnimation(intent)
                        binding.etPhoneNo.text?.clear()
//                        finish()

                    }

                    Resource.Status.ERROR -> {
                        dialog.dismiss()
                        binding.layoutMain.snackBarWithAction(this@LoginActivity,resource.data?.error?.customMessage.toString())

                    }

                    Resource.Status.LOADING -> {

                    }
                }
            }
        }

        lifecycleScope.launch {
            loginViewModel.phoneAvailableResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        dialog.dismiss()
                        val intent = Intent(this@LoginActivity, OtpVerificationActivity::class.java)
                        intent.putExtra(PHONE_NO, binding.etPhoneNo.text.toString())
                        intent.putExtra(IS_FROM_SIGN_UP, isFromSignUp)
                        startActivityWithAnimation(intent)
                        finish()

                    }

                    Resource.Status.ERROR -> {
                        dialog.dismiss()
                        binding.layoutMain.snackBarWithAction(this@LoginActivity,resource.data?.error?.customMessage.toString())
                    }

                    Resource.Status.LOADING -> {
                    }
                }
            }
        }
    }

    @SuppressLint("HardwareIds", "LogNotTimber")
    override fun initView() {
        isFromLogin = intent.getBooleanExtra(IS_FROM_LOGIN, false)
        isFromSignUp = intent.getBooleanExtra(IS_FROM_SIGN_UP, false)

        Log.d(THIS_TAG, "isFromLogin: $isFromLogin")
        Log.d(THIS_TAG, "isFromSignUp: $isFromSignUp")
        if (isFromLogin) {
            setLoginUI()
        }
        if (isFromSignUp) {
            setSignUpUI()
        }
    }

    private fun setSignUpUI() {
        setToolBar(
            binding.mToolbar,
            isBackEnable = true,
            isTitleEnable = false,
            isTitleTroubleEnable = true,
            titleText = "",
            titleTrouble = getString(R.string.trouble_signing_up_with_ques),
            isSkipVisible = false,
            titleSkipText = "",
            isFriendsVisible = false,
            isLogoVisible = false,
            isAddGroup = false
        )
        binding.apply {
            lblWlcmBack.text = getString(R.string.welcome_to_nudge_up)
            lblEnterPhone.text = getString(R.string.enter_your_phone_number_to_sign_up)
        }
    }

    private fun setLoginUI() {
        setToolBar(
            binding.mToolbar,
            isBackEnable = true,
            isTitleEnable = false,
            isTitleTroubleEnable = true,
            titleText = "",
            titleTrouble = getString(R.string.trouble_in_logging_in_with_ques),
            isSkipVisible = false,
            titleSkipText = "",
            isFriendsVisible = false,
            isLogoVisible = false,
            isAddGroup = false
        )
        binding.apply {
            lblWlcmBack.text = getString(R.string.welcome_back)
            lblEnterPhone.text = getString(R.string.enter_your_phone_number_to_log_in)
        }
    }
}