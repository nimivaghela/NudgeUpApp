package com.app.collt.ui.auth.create_profile

import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityCreateProfileBinding
import com.app.collt.domain.models.request.CreateProfileRequestModel
import com.app.collt.domain.models.response.LoginModel
import com.app.collt.extension.formatText
import com.app.collt.extension.hideKeyboard
import com.app.collt.extension.showProgress
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.choose_username.ChooseUsernameActivity
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.Calendar
import java.util.Date

class CreateProfileActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by viewModel()

    override val binding: ActivityCreateProfileBinding by binding(R.layout.activity_create_profile)

    override fun handleListener() {
        binding.apply {
            btnNext.setOnClickListener {
                if (isValid()) {
                    dialog = showProgress(this@CreateProfileActivity)
                    callUserProfileAPI()
                }
            }

            etDob.setOnClickListener {
                hideKeyboard()
                showDatePicker()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(
                this@CreateProfileActivity, /*R.style.MyDatePicker,*/
                { _, year, month, day ->
                    binding.etDob.setText(
                        "$year-${
                            formatText(month + 1)
                        }-${formatText(day)}"
                    )
                }, year, month, day
            )
        val maxDate = Calendar.getInstance()
        maxDate[Calendar.DAY_OF_MONTH] = day
        maxDate[Calendar.MONTH] = month
        maxDate[Calendar.YEAR] = year - 13

        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
//        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
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

    private fun callUserProfileAPI() {
        val createProfileRequestModel: CreateProfileRequestModel = CreateProfileRequestModel(
            name = binding.etName.text.toString(),
            birthDate = binding.etDob.text.toString()
        )
        loginViewModel.createUserProfile(
            createProfileRequestModel
        )
    }

    private fun isValid(): Boolean {
        binding.apply {
            return if (etName.text.toString().isEmpty()) {
                binding.layoutMain.snackBarWithAction(
                    this@CreateProfileActivity,
                    getString(R.string.error_enter_your_name)
                )
                false
            } else if (etDob.text.toString().isEmpty()) {
                binding.layoutMain.snackBarWithAction(
                    this@CreateProfileActivity,
                    getString(R.string.error_select_dob)
                )
                false
            } else {
                true
            }
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.createUserProfileResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        Timber.d("initObserver: SUCCESS")
                        dialog.dismiss()
                        val intent =
                            Intent(this@CreateProfileActivity, ChooseUsernameActivity::class.java)
                        startActivityWithAnimation(intent)
                        finish()
                    }

                    Resource.Status.ERROR -> {
                        dialog.dismiss()
                        binding.layoutMain.snackBarWithAction(
                            this@CreateProfileActivity,
                            resource.data?.error.toString()
                        )
                        Timber.d("initObserver: ERROR")
                    }

                    Resource.Status.LOADING -> {
                        Timber.d("initObserver: LOADING")
                    }
                }
            }
        }
    }

    override fun initView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isDialogInitialized)
            dialog.dismiss()
    }
}