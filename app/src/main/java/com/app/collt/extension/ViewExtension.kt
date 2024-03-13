package com.app.collt.extension

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import com.app.collt.R

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}



fun formatText(text: Int): String {
    return String.format("%02d", text)
}

fun showProgress(activity: Activity): Dialog {
    val dialog: Dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(0))
    dialog.setContentView(R.layout.item_progress_bar)
    dialog.setCancelable(false)
    dialog.show()
    return dialog
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    // Does not work yet, https://issuetracker.google.com/issues/240585930
    // SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    // Does not work yet, https://issuetracker.google.com/issues/240585930
    // SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}




