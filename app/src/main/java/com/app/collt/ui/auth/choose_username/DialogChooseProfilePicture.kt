package com.app.collt.ui.auth.choose_username

import android.content.Context
import android.view.LayoutInflater
import com.app.collt.R
import com.app.collt.databinding.DialogChooseProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogChooseProfilePicture(
    context: Context,
    var list: ArrayList<String>,
    var chooseProfileDialogInterface: ChooseProfileDialogInterface
) :
    BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme) {
    lateinit var dialogBinding: DialogChooseProfileBinding

    companion object {
        const val TAG = "DialogChooseProfilePicture"
    }

    init {
        initView()
    }

    private fun initView() {
        dialogBinding =
            DialogChooseProfileBinding.inflate(LayoutInflater.from(context), null, false)
        setContentView(dialogBinding.root)
        setCanceledOnTouchOutside(true)

        dialogBinding.tvTakePhoto.setOnClickListener {
            chooseProfileDialogInterface.askForPermission(true, false)
            dismiss()
        }

        dialogBinding.tvPhotoLibrary.setOnClickListener {
            chooseProfileDialogInterface.askForPermission(false, true)
            dismiss()
        }

        dialogBinding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    interface ChooseProfileDialogInterface {
        fun askForPermission(isFromCamera: Boolean = false, isFromGallery: Boolean = false)
    }
}