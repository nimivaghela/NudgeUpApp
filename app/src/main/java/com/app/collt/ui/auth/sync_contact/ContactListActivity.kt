package com.app.collt.ui.auth.sync_contact

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityContactListBinding
import com.app.collt.domain.models.ContactModel
import com.app.collt.domain.models.request.SendContactListRequestModel
import com.app.collt.extension.logError
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.helper.StatusCode
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.ui.auth.onboarding_welcome.BoardingWelcomeActivity
import com.app.collt.utils.PermissionCallback
import com.app.collt.utils.PermissionHelper
import com.app.collt.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class ContactListActivity : BaseActivity() {
    private var contactModelArrayList: ArrayList<ContactModel> = arrayListOf()
    private val loginViewModel: LoginViewModel by viewModel()
    lateinit var sendContactListRequestModel: SendContactListRequestModel

    override val binding: ActivityContactListBinding by binding(R.layout.activity_contact_list)

    override fun handleListener() {
        binding.mToolbar.tvSkip.setOnClickListener {
//            mUserHolder.setAfterLoginWhereToNavigate(IS_FROM_WALKTHROUGH)
            val intent =
                Intent(this@ContactListActivity, BoardingWelcomeActivity::class.java)
            startActivityWithAnimation(intent)
            finish()
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.sendContactListResponseModel.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.data?.let { dataModel ->
                            getSyncContactsList()
                        }
                        Timber.d("initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
                        binding.layoutMain.snackBarWithAction(
                            this@ContactListActivity,
                            resource.message.toString()
                        )
                        Timber.d("initObserver: ERROR")

                        when (resource.data?.error?.errorCode) {
                            StatusCode.STATUS_401 -> {
                                autoLogout()
                            }

                            StatusCode.STATUS_409 -> {
                                loginFirst()
                            }

                            StatusCode.STATUS_422 -> {
                                onOTPExpire(resource.error?.customMessage)
                            }

                            StatusCode.STATUS_412 -> {
                                onVerificationError()
                            }

                            StatusCode.STATUS_426 -> {
                                onSubscribeRequired(resource.error?.customMessage)
                            }

                            else -> {
                                logError(msg = "onError: ${resource.error?.customMessage}")
                            }
                        }
                    }

                    Resource.Status.LOADING -> {
                        Timber.d("initObserver: LOADING")
                    }
                }
            }
        }

        lifecycleScope.launch {
            loginViewModel.getSyncContactResponseModel.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.data?.let { dataModel ->
                            if (dataModel.users.size > 0) {
                                val alreadyUserContactListAdapter =
                                    AlreadyUserContactListAdapter(dataModel.users)
                                binding.rvContactOnNudgeUp.adapter = alreadyUserContactListAdapter
                            }
                        }
                        Timber.d("initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
                        binding.layoutMain.snackBarWithAction(
                            this@ContactListActivity,
                            resource.message.toString()
                        )
                        Timber.d("initObserver: ERROR")
                        when (resource.error?.errorCode) {
                            StatusCode.STATUS_401 -> {
                                autoLogout()
                            }

                            StatusCode.STATUS_409 -> {
                                loginFirst()
                            }

                            StatusCode.STATUS_422 -> {
                                onOTPExpire(resource.error?.customMessage)
                            }

                            StatusCode.STATUS_412 -> {
                                onVerificationError()
                            }

                            StatusCode.STATUS_426 -> {
                                onSubscribeRequired(resource.error?.customMessage)
                            }

                            else -> {
                                logError(msg = "onError: ${resource.error?.customMessage}")
                            }
                        }
                    }

                    Resource.Status.LOADING -> {
                        Timber.d("initObserver: LOADING")
                    }
                }
            }
        }


    }

    private fun getSyncContactsList() {
        loginViewModel.getSyncContactList(this@ContactListActivity)
    }

    override fun initView() {
        setToolBar(
            binding.mToolbar,
            isBackEnable = false,
            isTitleEnable = false,
            isTitleTroubleEnable = false,
            "", "", isSkipVisible = true,
            titleSkipText = getString(R.string.next),
            isFriendsVisible = false,
            isLogoVisible = false,
            isAddGroup = false
        )

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            loadContactsData()
        } else {
            permissionHelper = PermissionHelper(
                this, arrayOf(Manifest.permission.READ_CONTACTS), 1001
            )
            askContactPermission()
        }

    }

    private fun askContactPermission() {
        permissionHelper.requestPermissions(object : PermissionCallback {
            override fun onPermissionGranted() {
                loadContactsData()
            }

            override fun onPermissionDenied() {
                Toast.makeText(this@ContactListActivity, "Permission denied", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onPermissionDeniedBySystem() {
                Toast.makeText(
                    this@ContactListActivity,
                    "Permission denied by system",
                    Toast.LENGTH_LONG
                ).show()

                permissionHelper.openSettings()
            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
                Toast.makeText(
                    this@ContactListActivity,
                    "Permission individual granted",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onPermissionNotFoundInManifest() {
                Toast.makeText(this@ContactListActivity, "Permission not found", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    @SuppressLint("Range")
    private fun loadContactsData() {
        contactModelArrayList = arrayListOf()
        sendContactListRequestModel = SendContactListRequestModel()

        val contactCursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (contactCursor!!.count > 0) {
            while (contactCursor.moveToNext()) {
                val contactModel = ContactModel()
                val id: String =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt() > 0
                ) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        arrayOf(id), null
                    )
                    while (phoneCursor!!.moveToNext()) {
                        var phoneNumber =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))

                        contactModel.firstName = name
                        if (phoneNumber == null)
                            phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contactModel.contactNumber = phoneNumber
                        contactModel.phoneNumber = phoneNumber.replace("[^0-9]".toRegex(), "")

                        val emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                            arrayOf(id), null
                        )
                        while (emailCursor!!.moveToNext()) {
                            val email =
                                emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))

                            contactModel.email = email
                        }
                        contactModelArrayList.add(contactModel)
                        emailCursor.close()
                    }
                    phoneCursor.close()
                }
            }
            contactCursor.close()
        }
        contactModelArrayList.sortBy {
            it.firstName
        }
        sendContactListRequestModel.contacts = contactModelArrayList
        callSendContactAPI()

        val contactListAdapter = ContactListAdapter(contactModelArrayList)
        binding.rvInviteFrnd.adapter = contactListAdapter
    }

    private fun callSendContactAPI() {
        loginViewModel.sendContactList(sendContactListRequestModel)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

}