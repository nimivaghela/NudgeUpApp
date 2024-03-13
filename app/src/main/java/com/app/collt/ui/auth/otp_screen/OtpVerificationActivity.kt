package com.app.collt.ui.auth.otp_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityOtpVerificaitonBinding
import com.app.collt.domain.models.request.LoginRequestModel
import com.app.collt.domain.models.response.LoginModel
import com.app.collt.extension.getPackageInfoCompat
import com.app.collt.extension.gone
import com.app.collt.extension.hideKeyboard
import com.app.collt.extension.logDebug
import com.app.collt.extension.logError
import com.app.collt.extension.showProgress
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.extension.visible
import com.app.collt.helper.ANDROID
import com.app.collt.helper.IS_FROM_LOGIN
import com.app.collt.helper.IS_FROM_SIGN_UP
import com.app.collt.helper.PHONE_NO
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.choose_profile_photo.ChooseProfilePictureActivity
import com.app.collt.ui.auth.choose_username.ChooseUsernameActivity
import com.app.collt.ui.auth.create_profile.CreateProfileActivity
import com.app.collt.ui.auth.home.HomeActivity
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.utils.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : BaseActivity() {
    private var phoneNo: String? = null
    var counter = 30
    private val loginViewModel: LoginViewModel by viewModel()
    private var loginRequestModel: LoginRequestModel? = null
    private var isFromLogin: Boolean = false
    private var isFromSignUp: Boolean = false
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override val binding: ActivityOtpVerificaitonBinding by binding(R.layout.activity_otp_verificaiton)

    private fun isValid(): Boolean {
        binding.apply {
            return if (etOtp.text.toString().isEmpty()) {
                binding.layoutMain.snackBarWithAction(
                    this@OtpVerificationActivity,
                    getString(R.string.error_enter_otp)
                )
                false
            } else if (etOtp.text.toString().length < 6) {
                binding.layoutMain.snackBarWithAction(
                    this@OtpVerificationActivity,
                    getString(R.string.error_enter_valid_otp)
                )
                false
            } else {
                true
            }
        }
    }

    override fun handleListener() {
        binding.apply {
            btnContinue.setOnClickListener {
                if (isValid()) {
                    dialog = showProgress(this@OtpVerificationActivity)
                    hideKeyboard()
                    verifyCode(binding.etOtp.text.toString())
                }
            }

            tvSendAgain.setOnClickListener {
                resendVerificationCode()
                counter = 30
                startTimer()
            }
        }
    }

    private fun manageNavigation() {
        if (mUserHolder.name.isNullOrEmpty()) {
            val intent = Intent(this@OtpVerificationActivity, CreateProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivityWithAnimation(intent)
        } else if (mUserHolder.userName.isNullOrEmpty()) {
            val intent = Intent(this@OtpVerificationActivity, ChooseUsernameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivityWithAnimation(intent)
        } else if (mUserHolder.image.isNullOrEmpty()) {
            val intent =
                Intent(this@OtpVerificationActivity, ChooseProfilePictureActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivityWithAnimation(intent)
        } else {
            val intent = Intent(this@OtpVerificationActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivityWithAnimation(intent)
        }
//        val intent = Intent(this@OtpVerificationActivity, CreateProfileActivity::class.java)
//        startActivityWithAnimation(intent)
//        finish()

        /* IS_FROM_CHOOSE_USERNAME -> {
             val intent =
                 Intent(this@OtpVerificationActivity, ChooseProfilePictureActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }

         IS_FROM_PROFILE_PIC -> {
             val intent =
                 Intent(this@OtpVerificationActivity, SyncContactActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }

         IS_FROM_SYNC_CONTACT -> {
             val intent =
                 Intent(this@OtpVerificationActivity, ContactListActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }

         IS_FROM_CONTACT_LIST -> {
             val intent =
                 Intent(this@OtpVerificationActivity, BoardingWelcomeActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }

         IS_FROM_WALKTHROUGH -> {
             val intent =
                 Intent(this@OtpVerificationActivity, HomeActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }

         IS_FROM_HOME -> {
             val intent =
                 Intent(this@OtpVerificationActivity, HomeActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }

         else -> {
             val intent =
                 Intent(this@OtpVerificationActivity, CreateProfileActivity::class.java)
             startActivityWithAnimation(intent)
             finish()
         }*/

    }

    private fun startTimer() {
        binding.apply {
            object : CountDownTimer(31000, 1000) {
                override fun onTick(p0: Long) {
                    countDownTimer.text = counter.toString()
                    counter--
                    countDownTimer.visible()
                    tvDidNtReceiveCode.gone()
                    tvSendAgain.gone()
                }

                override fun onFinish() {
                    countDownTimer.gone()
                    tvDidNtReceiveCode.visible()
                    tvSendAgain.visible()
                }
            }.start()
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.loginResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        dialog.hide()
                        resource.data?.data?.let { loginModel ->
                            storeResponseInLocalStorage(loginModel)
                        }
                        manageNavigation()

                        Timber.d("initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
                        dialog.hide()
                        binding.layoutMain.snackBarWithAction(
                            this@OtpVerificationActivity,
                            resource.message.toString()
                        )
                        Timber.d("initObserver: ERROR")
                    }

                    Resource.Status.LOADING -> {
                        Timber.d("initObserver: LOADING")
                    }
                }
            }
        }

        lifecycleScope.launch {
            loginViewModel.signUpResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        dialog.hide()
                        mUserHolder.setUserData(
                            "", "", resource.data?.data?.authToken, "", "", ""
                        )
                        manageNavigation()
                        Timber.d("initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
                        dialog.hide()
                        Timber.d("initObserver: ERROR")
                        binding.layoutMain.snackBarWithAction(
                            this@OtpVerificationActivity,
                            resource.data?.error?.customMessage.toString()
                        )

                    }

                    Resource.Status.LOADING -> {
                        Timber.d("initObserver: LOADING")
                    }
                }
            }
        }
    }

    private fun storeResponseInLocalStorage(loginModel: LoginModel?) {
        loginModel?.let {
            if (it.userMedia!!.size > 0) {
                mUserHolder.setUserData(
                    it.name,
                    it.phone,
                    it.authToken,
                    it.birthDate,
                    it.userName,
                    it.userMedia!![it.userMedia!!.size - 1]?.media
                )
            } else {
                mUserHolder.setUserData(
                    it.name, it.phone, it.authToken, it.birthDate, it.userName, it.image
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (intent.hasExtra(PHONE_NO)) {
            phoneNo = intent.getStringExtra(PHONE_NO)
        }
        if (intent.hasExtra(IS_FROM_LOGIN)) {
            isFromLogin = intent.getBooleanExtra(IS_FROM_LOGIN, false)
        }
        if (intent.hasExtra(IS_FROM_SIGN_UP)) {
            isFromSignUp = intent.getBooleanExtra(IS_FROM_SIGN_UP, false)
        }

        mAuth = FirebaseAuth.getInstance()
        mAuth.setLanguageCode("en")

        sendVerificationCode()

        binding.apply {
            etOtp.requestFocus()
            this.lblPhoneNo.text = "+91 $phoneNo"
            setToolBar(
                binding.mToolbar,
                isBackEnable = true,
                isTitleEnable = false,
                isTitleTroubleEnable = false,
                titleText = "",
                titleTrouble = "",
                isSkipVisible = false,
                titleSkipText = "",
                isFriendsVisible = false,
                isLogoVisible = false,
                isAddGroup = false
            )

            etOtp.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.length == 6) {
                        hideKeyboard()
                        setButton(true)
                    } else {
                        setButton(false)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            countDownTimer.postDelayed({
                startTimer()
            }, 2000)
        }
    }

    private fun sendVerificationCode() {
        phoneNo?.let {
            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91$it") // Phone number to verify
                .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@OtpVerificationActivity) // Activity (for callback binding)
                .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun setButton(isEnable: Boolean) {
        if (isEnable) {
            binding.btnContinue.background =
                ContextCompat.getDrawable(this, R.drawable.bg_button_white)
            binding.btnContinue.setTextColor(getColor(R.color.colorPrimary))
        } else {
            binding.btnContinue.background =
                ContextCompat.getDrawable(this, R.drawable.bg_radius_gray_30dp)
            binding.btnContinue.setTextColor(getColor(R.color.white_30))
        }
    }

    private fun resendVerificationCode() {
        val optionsBuilder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$phoneNo") // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
        if (this::resendToken.isInitialized)
            optionsBuilder.setForceResendingToken(resendToken)
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted: ")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (isDialogInitialized)
                    dialog.hide()

                Log.d("TAG", "onVerificationFailed: ")

                binding.layoutMain.snackBarWithAction(
                    this@OtpVerificationActivity,
                    e.message.toString()
                )
            }

            override fun onCodeSent(s: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationId = s
                resendToken = token
                Log.d("TAG", "onCodeSent: ")
            }
        }

    private fun verifyCode(code: String) {
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, code) }
        credential?.let { signInWithCredential(it) }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getTokenId(task.result)
            } else {
                dialog.hide()
                binding.layoutMain.snackBarWithAction(this, task.exception?.message.toString())
            }
        }
    }

    private fun getTokenId(result: AuthResult?) {
        result?.user?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                if (isFromLogin) {
                    callLoginAPI(it.result.token.toString())
                }
                if (isFromSignUp) {
                    callSignUpAPI(it.result.token.toString())
                }
            }
        }
    }

    private fun callSignUpAPI(token: String) {
        val map: HashMap<String, String> = hashMapOf()
        phoneNo?.let { map.put("phone", it) }
        map["countryCode"] = "+91"
        map["appVersion"] = packageManager.getPackageInfoCompat(packageName).versionName
        map["deviceToken"] = mUserHolder.fcmToken.toString()
        map["deviceType"] = ANDROID
        map["token"] = token
        map["timeZone"] = TimeZone.getDefault().id

        loginViewModel.callSignUpAPI(map, this)
    }

    private fun callLoginAPI(token: String) {
        loginRequestModel = LoginRequestModel(
            phone = phoneNo,
            token = token,
            deviceType = ANDROID,
            deviceToken = mUserHolder.fcmToken.toString(),
            appVersion = packageManager.getPackageInfoCompat(packageName).versionName,
            timeZone = TimeZone.getDefault().id
        )

        loginRequestModel?.let {
            loginViewModel.callLoginAPI(it, this@OtpVerificationActivity, mDisposable)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isDialogInitialized)
            dialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isDialogInitialized)
            dialog.dismiss()
    }

    override fun onStop() {
        super.onStop()
        if (isDialogInitialized)
            dialog.dismiss()
    }
}