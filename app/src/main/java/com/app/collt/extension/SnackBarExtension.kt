package com.app.collt.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.app.collt.R
import com.google.android.material.snackbar.Snackbar


fun View.snackBarWithAction(activity: Activity, msg: String, duration: Int = Snackbar.LENGTH_LONG) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    val snackbar: Snackbar = Snackbar.make(this, msg, duration)
    val snackbarview: View = snackbar.view
    val tv = snackbarview.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    tv.setTextColor(context.getColor(R.color.white))
    tv.textSize = 12F
    tv.maxLines = 4
    tv.setBackgroundColor(
        ContextCompat.getColor(
            this.context,
            R.color.colorPrimaryVariant
        )
    )
    snackbar.setAction(context.getString(R.string.dismiss)) {
        snackbar.dismiss()
    }
    snackbar.setBackgroundTint(context.getColor(R.color.colorPrimaryVariant))
    snackbar.setActionTextColor(ContextCompat.getColor(this.context, R.color.vibrant_purple_50))
    tv.setBackgroundResource(R.drawable.bg_snackbar_corner)
    ViewCompat.setElevation(snackbarview, 6f)
    snackbar.show()
}