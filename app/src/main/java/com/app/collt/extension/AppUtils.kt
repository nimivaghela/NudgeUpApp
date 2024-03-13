package com.app.collt.extension

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar
import com.app.collt.R

class AppUtils {
    companion object {
        lateinit var context: Context
        var dialog: Dialog = Dialog(context)

        fun showProgress(mContext: Context): Dialog {
            context = mContext
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.item_progress_bar)
            var progressBar: ProgressBar = dialog.findViewById(R.id.progressBar)
//    progressBar.indeterminateDrawable.colorFilter =
//        (context.resources.getColor(R.color.colorPrimary))
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }

    }

}