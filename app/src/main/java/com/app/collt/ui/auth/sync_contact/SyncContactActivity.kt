package com.app.collt.ui.auth.sync_contact

import android.Manifest
import android.content.Intent
import com.app.collt.R
import com.app.collt.databinding.ActivitySyncContactBinding
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.utils.PermissionCallback
import com.app.collt.utils.PermissionHelper

class SyncContactActivity : BaseActivity() {

    override val binding: ActivitySyncContactBinding by binding(R.layout.activity_sync_contact)

    override fun handleListener() {
        binding.apply {
            btnFindMyFriend.setOnClickListener {
                askContactPermission()
            }
        }
    }

    override fun initObserver() {

    }

    override fun initView() {
//        mUserHolder.setAfterLoginWhereToNavigate(IS_FROM_SYNC_CONTACT)
        setToolBar(
            binding.mToolbar,
            isBackEnable = false,
            isTitleEnable = false,
            isTitleTroubleEnable = false,
            "",
            "",
            isSkipVisible = true,
            titleSkipText = getString(R.string.skip),
            isFriendsVisible = false,
            isLogoVisible = false,
            isAddGroup = false
        )
        permissionHelper = PermissionHelper(
            this, arrayOf(Manifest.permission.READ_CONTACTS), 1001
        )


    }

    private fun askContactPermission() {
        permissionHelper?.requestPermissions(object : PermissionCallback {
            override fun onPermissionGranted() {
                val intent =
                    Intent(this@SyncContactActivity, ContactListActivity::class.java)
                startActivityWithAnimation(intent)
                finish()
            }

            override fun onPermissionDenied() {
//                Toast.makeText(this@SyncContactActivity, "Permission denied", Toast.LENGTH_LONG)
//                    .show()
            }

            override fun onPermissionDeniedBySystem() {
//                Toast.makeText(
//                    this@SyncContactActivity,
//                    "Permission denied by system",
//                    Toast.LENGTH_LONG
//                ).show()

                permissionHelper?.openSettings()
            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
//                Toast.makeText(
//                    this@SyncContactActivity,
//                    "Permission individual granted",
//                    Toast.LENGTH_LONG
//                ).show()
            }

            override fun onPermissionNotFoundInManifest() {
//                Toast.makeText(this@SyncContactActivity, "Permission not found", Toast.LENGTH_LONG)
//                    .show()
            }
        })
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