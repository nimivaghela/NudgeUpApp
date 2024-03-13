package com.app.collt.ui.auth.choose_username

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityChooseUsernameBinding
import com.app.collt.extension.gone
import com.app.collt.extension.invisible
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.extension.visible
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.choose_profile_photo.ChooseProfilePictureActivity
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ChooseUsernameActivity : BaseActivity() {
    var userNameList: ArrayList<String> = arrayListOf()
    private val loginViewModel: LoginViewModel by viewModel()

    private val userListAdapter = UserListAdapter {
        binding.etUserName.setText(it)
    }
    override val binding: ActivityChooseUsernameBinding by binding(R.layout.activity_choose_username)

    private fun isValid(): Boolean {
        binding.apply {
            return if (etUserName.text.toString().isEmpty()) {
                binding.layoutMain.snackBarWithAction(
                    this@ChooseUsernameActivity,
                    getString(R.string.error_choose_user_name)
                )
                false
            } else {
                true
            }
        }
    }

    override fun handleListener() {
        binding.btnNext.setOnClickListener {
            if (isValid()) {
//                mUserHolder.setAfterLoginWhereToNavigate(IS_FROM_CHOOSE_USERNAME)
                loginViewModel.setUserName(binding.etUserName.text.toString())
            }
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.checkAvailableUserNameResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.etUserName.setTextColor(getColor(R.color.turquoise))
                        binding.tvSuggestions.visible()
                        binding.rvSuggestions.visible()
                        binding.ivCorrect.visible()
                        binding.ivCorrect.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@ChooseUsernameActivity,
                                R.drawable.baseline_check_circle_24
                            )
                        )
                        binding.tvAlreadyTaken.gone()
                    }

                    Resource.Status.ERROR -> {
                        if (resource.message!!.contains("409")) {
                            binding.etUserName.setTextColor(getColor(R.color.vibrant_pink))
                            binding.tvAlreadyTaken.visible()
                            binding.ivCorrect.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@ChooseUsernameActivity,
                                    R.drawable.baseline_cancel_24
                                )
                            )
                            binding.ivCorrect.visible()
                        } else {
                            binding.layoutMain.snackBarWithAction(
                                this@ChooseUsernameActivity,
                                resource.message.toString()
                            )
                        }
                        Timber.d("initObserver: ERROR")
                    }

                    Resource.Status.LOADING -> {
                        Timber.d("initObserver: LOADING")
                    }
                }
            }
        }
        lifecycleScope.launch {
            loginViewModel.availableUserNameListResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        userNameList.clear()
                        resource.data?.data?.let { uNameList ->
                            userNameList.addAll(uNameList)
                        }
                        userListAdapter.updateList(userNameList)
                    }

                    Resource.Status.ERROR -> {
                        binding.layoutMain.snackBarWithAction(
                            this@ChooseUsernameActivity,
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
            loginViewModel.setUserNameResponseFlow.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        mUserHolder.setLoginStatus(true)
                        val intent =
                            Intent(
                                this@ChooseUsernameActivity,
                                ChooseProfilePictureActivity::class.java
                            )
                        startActivityWithAnimation(intent)
                        finish()
                    }

                    Resource.Status.ERROR -> {
                        binding.layoutMain.snackBarWithAction(
                            this@ChooseUsernameActivity,
                            resource.data?.error?.customMessage.toString()
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
            tvSuggestions.invisible()
            rvSuggestions.invisible()
            tvAlreadyTaken.gone()

            binding.tvAlreadyTaken.text =
                getString(R.string.already_taken) + String(Character.toChars(0x1F605))

            etUserName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        if (p0.length >= 1) {
                            callCheckUserNameAvailableAPI(p0.toString() ?: "")
                            callFetchAvailableUserNameListAPI(p0.toString() ?: "")
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.rvSuggestions.visible()
                    binding.tvSuggestions.visible()
                }
            })
            rvSuggestions.adapter = userListAdapter
        }
    }

    private fun callFetchAvailableUserNameListAPI(name: String) {
        loginViewModel.getAvailableUserNameList(name)
    }

    private fun callCheckUserNameAvailableAPI(name: String) {
        loginViewModel.checkUserNameAvailable(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isDialogInitialized)
            dialog.dismiss()
    }
}