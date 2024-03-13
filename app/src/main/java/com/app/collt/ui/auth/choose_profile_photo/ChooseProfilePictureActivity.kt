package com.app.collt.ui.auth.choose_profile_photo

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityChooseProfilePictureBinding
import com.app.collt.extension.gone
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.extension.visible
import com.app.collt.helper.BITMAP_IMAGE
import com.app.collt.helper.IMAGE_URI
import com.app.collt.helper.THIS_TAG
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.choose_username.DialogChooseProfilePicture
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.ui.auth.sync_contact.ContactListActivity
import com.app.collt.ui.auth.sync_contact.SyncContactActivity
import com.app.collt.utils.PermissionCallback
import com.app.collt.utils.PermissionHelper
import com.app.collt.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar


class ChooseProfilePictureActivity : BaseActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var isButtonEnable: Boolean = false
    private val loginViewModel: LoginViewModel by viewModel()
    override val binding: ActivityChooseProfilePictureBinding by binding(R.layout.activity_choose_profile_picture)
    lateinit var requestBody: RequestBody

    companion object {
        var bitmap: Bitmap? = null
        var uri: Uri? = null
        var byteArray: ByteArray? = null
    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.addProfilePicResponseModel.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.data?.let { loginModel ->
                            val intent = Intent(
                                this@ChooseProfilePictureActivity, SyncContactActivity::class.java
                            )
                            startActivityWithAnimation(intent)
                            finish()
                        }

                        Timber.d("initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
                        binding.layoutMain.snackBarWithAction(
                            this@ChooseProfilePictureActivity, resource.message.toString()
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initView() {
        setToolBar(
            binding.mToolbar,
            isBackEnable = true,
            isTitleEnable = false,
            isTitleTroubleEnable = false,
            titleText = "",
            titleTrouble = "",
            isSkipVisible = true,
            titleSkipText = getString(R.string.skip),
            isFriendsVisible = false,
            isLogoVisible = false,
            isAddGroup = false
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            permissionHelper = PermissionHelper(
                this@ChooseProfilePictureActivity, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ), 1002
            )
        } else {
            permissionHelper = PermissionHelper(
                this@ChooseProfilePictureActivity, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                ), 1002
            )
        }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Log.d(THIS_TAG, "initView: permission granted")
                } else {
                    Log.d(THIS_TAG, "initView: permission denied")

                }
            }

    }

    override fun handleListener() {
        binding.apply {
            mToolbar.tvSkip.setOnClickListener {
                val intent =
                    Intent(this@ChooseProfilePictureActivity, ContactListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivityWithAnimation(intent)
            }

            ivProfileBg.setOnClickListener {
                showChooseProfileDialog()
            }

            ivAddProfile.setOnClickListener {
                showChooseProfileDialog()
            }

            btnNext.setOnClickListener {
                if (isButtonEnable) {
                    val intent = Intent(
                        this@ChooseProfilePictureActivity, CropPhotoActivity::class.java
                    )
                    byteArray?.let {
                        intent.putExtra(BITMAP_IMAGE, byteArray)
                    }
                    uri?.let {
                        intent.putExtra(IMAGE_URI, uri.toString())
                    }
                    startActivityWithAnimation(intent)
                }
            }

            ivClose.setOnClickListener {
                ivProfileBg.setImageDrawable(null)
                ivClose.gone()
                setButton(false)
            }
        }
    }

    private fun showChooseProfileDialog() {
        DialogChooseProfilePicture(
            this,
            arrayListOf(),
            object : DialogChooseProfilePicture.ChooseProfileDialogInterface {
                override fun askForPermission(isFromCamera: Boolean, isFromGallery: Boolean) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        if ((ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity, Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED)
                        ) {
                            if (isFromCamera) {
                                openCamera()
                            }
                            if (isFromGallery) {
                                openGallery()
                            }
                        } else {
                            askPermissionForFileReadWrite(isFromCamera, isFromGallery)
                        }
                    } else {
                        if ((ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity, Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity,
                                Manifest.permission.READ_MEDIA_IMAGES
                            ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity,
                                Manifest.permission.READ_MEDIA_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                                this@ChooseProfilePictureActivity,
                                Manifest.permission.READ_MEDIA_VIDEO
                            ) == PackageManager.PERMISSION_GRANTED)
                        ) {
                            if (isFromCamera) {
                                openCamera()
                            }
                            if (isFromGallery) {
                                openGallery()
                            }
                        } else {
                            askPermissionForFileReadWrite(isFromCamera, isFromGallery)
                        }
                    }
                }
            }).apply {
            show()
        }
    }

    private fun askPermissionForFileReadWrite(isFromCamera: Boolean, isFromGallery: Boolean) {
        permissionHelper.requestPermissions(object : PermissionCallback {
            override fun onPermissionGranted() {
                if (isFromCamera) {
                    openCamera()
                }
                if (isFromGallery) {
                    openGallery()
                }
                Log.d(THIS_TAG, "onPermissionGranted: ")
            }

            override fun onPermissionDenied() {
                Log.d(THIS_TAG, "onPermissionDenied: ")
            }

            override fun onPermissionDeniedBySystem() {
                Log.d(THIS_TAG, "onPermissionDeniedBySystem: ")
                permissionHelper.openSettings()
            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
                Log.d(THIS_TAG, "onIndividualPermissionGranted: ")
            }

            override fun onPermissionNotFoundInManifest() {
                Log.d(THIS_TAG, "onPermissionNotFoundInManifest: ")
            }
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityLauncher.launch(Intent.createChooser(intent, "Select picture"))
    }

    private var activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            activityResult.data?.let {
                if (it.data == null) {
                    bitmap = it.extras!!["data"] as Bitmap

                    val bStream = ByteArrayOutputStream()
                    bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, bStream)
                    byteArray = bStream.toByteArray()

                    binding.ivProfileBg.setImageBitmap(bitmap)
                    binding.ivClose.visible()
                    setButton(true)
                } else {
                    uri = it.data
                    binding.ivProfileBg.setImageURI(it.data)
                    binding.ivClose.visible()
                    setButton(true)
                }
            }
        } else {

        }
    }

    private fun addProfileAPI(intentData: Intent) {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.addFormDataPart("index", "0").addFormDataPart("mediaType", "IMAGE")
            .addFormDataPart("isProfilePic", "true")

        lateinit var featuredImage: File

        if (intentData.data != null) {
            featuredImage = File(getRealPathFromURI(this, intentData.data!!))
            builder.addFormDataPart(
                "media", featuredImage.name, RequestBody.create(MultipartBody.FORM, featuredImage)
            )
        } else {
            val tempUri: Uri? =
                getImageUri(applicationContext, intentData.extras!!["data"] as Bitmap)

            featuredImage = File(getRealPathFromURI(this, tempUri!!))

            val bmp = BitmapFactory.decodeFile(featuredImage.absolutePath)
            val bos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)

            builder.addFormDataPart(
                "media",
                featuredImage.name,
                RequestBody.create(MultipartBody.FORM, bos.toByteArray())
            )
        }

        requestBody = builder.build()
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = Images.Media.insertImage(
            inContext.contentResolver, inImage, "Title_${Calendar.getInstance().time}", null
        )
        Log.d(THIS_TAG, "getImageUri: Title_${Calendar.getInstance().time}")
        return Uri.parse(path)
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, null, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }

                    isDownloadsDocument(uri) -> {
                        val fileName = getFilePath(context, uri)
                        if (fileName != null) {
                            return Environment.getExternalStorageDirectory()
                                .toString() + "/Download/" + fileName
                        }
                        var id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            id = id.replaceFirst("raw:".toRegex(), "")
                            val file = File(id)
                            if (file.exists()) return id
                        }
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                        return getDataColumn(context, contentUri, null, null)
                    }

                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }

                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }

                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            }

            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context, uri, null, null
                )
            }

            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    private fun setButton(isEnable: Boolean) {
        if (isEnable) {
            isButtonEnable = true
            binding.btnNext.background = ContextCompat.getDrawable(this, R.drawable.bg_button_white)
            binding.btnNext.setTextColor(getColor(R.color.colorPrimary))
        } else {
            isButtonEnable = false
            binding.btnNext.background =
                ContextCompat.getDrawable(this, R.drawable.bg_radius_gray_30dp)
            binding.btnNext.setTextColor(getColor(R.color.white_30))
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityLauncher.launch(cameraIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

}