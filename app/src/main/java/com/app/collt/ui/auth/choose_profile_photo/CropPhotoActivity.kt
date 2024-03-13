package com.app.collt.ui.auth.choose_profile_photo

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.app.collt.R
import com.app.collt.databinding.ActivityCropPhotoBinding
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.helper.BITMAP_IMAGE
import com.app.collt.helper.IMAGE_URI
import com.app.collt.helper.THIS_TAG
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import com.app.collt.ui.auth.sync_contact.SyncContactActivity
import com.app.collt.utils.Resource
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File


class CropPhotoActivity : BaseActivity() {
    private val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage"
    private lateinit var requestBody: RequestBody
    private val loginViewModel: LoginViewModel by viewModel()

    override val binding: ActivityCropPhotoBinding by binding(R.layout.activity_crop_photo)

    override fun handleListener() {

    }

    override fun initObserver() {
        lifecycleScope.launch {
            loginViewModel.addProfilePicResponseModel.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        resource.data?.data?.let { loginModel ->
                            val intent = Intent(
                                this@CropPhotoActivity, SyncContactActivity::class.java
                            )
                            startActivityWithAnimation(intent)
                            finish()
                        }

                        Timber.d("initObserver: SUCCESS")
                    }

                    Resource.Status.ERROR -> {
                        binding.layoutMain.snackBarWithAction(
                            this@CropPhotoActivity, resource.message.toString()
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
            isTitleTroubleEnable = false,
            titleText = "",
            titleTrouble = "",
            isSkipVisible = true,
            titleSkipText = getString(R.string.skip),
            isFriendsVisible = false,
            isLogoVisible = false,
            isAddGroup = true
        )

        if (intent.hasExtra(BITMAP_IMAGE)) {
            val bmp: Bitmap
            val byteArray = intent.getByteArrayExtra(BITMAP_IMAGE)
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            var uri: Uri? = getImageUri(this, bmp)
            if (uri != null) {
                startCrop(uri)
            }
        }
        if (intent.hasExtra(IMAGE_URI)) {
            val uri = intent.getStringExtra(IMAGE_URI)

            startCrop(Uri.parse(uri))
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            addProfileAPI(result, resultUri)
//            ResultActivity.startWithUri(this@SampleActivity, resultUri)
        } else {
            Toast.makeText(
                this@CropPhotoActivity,
                R.string.toast_cannot_retrieve_cropped_image,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP) {
            handleCropResult(data!!)
        }
    }

    private fun startCrop(uri: Uri) {
        var destinationFileName: String = SAMPLE_CROPPED_IMAGE_NAME

        destinationFileName += ".png"

        var uCrop: UCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop = basisConfig(uCrop)
        uCrop = advancedConfig(uCrop)


        // else start uCrop Activity
        uCrop.start(this@CropPhotoActivity)
        uCrop.fragment.setCallback(object : UCropFragmentCallback {
            override fun loadingProgress(showLoader: Boolean) {

            }

            override fun onCropFinish(result: UCropFragment.UCropResult?) {
                Log.d(THIS_TAG, "onCropFinish: ")
            }
        })
    }

    private fun advancedConfig(uCrop: UCrop): UCrop {
        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)

        options.setCompressionQuality(90)
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(false)

        return uCrop.withOptions(options)
    }

    private fun basisConfig(uCrop: UCrop): UCrop {
        var uCrop = uCrop
        uCrop = uCrop.useSourceImageAspectRatio()

        return uCrop
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun addProfileAPI(intentData: Intent, uri: Uri?) {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.addFormDataPart("index", "0").addFormDataPart("mediaType", "IMAGE")
            .addFormDataPart("isProfilePic", "true")

        var featuredImage: File = File(getRealPathFromURI(this, uri!!))

        builder.addFormDataPart(
            "media", featuredImage.name, RequestBody.create(MultipartBody.FORM, featuredImage)
        )

        requestBody = builder.build()
        loginViewModel.addProfilePic(requestBody)

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

}