package com.app.collt.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import com.app.collt.R
import com.google.android.material.snackbar.Snackbar


object SnackbarHelper {
    fun configSnackbar(context: Context, snack: Snackbar) {
        addMargins(snack)
        setRoundBordersBg(context, snack)
        ViewCompat.setElevation(snack.view, 6f)
    }

    private fun addMargins(snack: Snackbar) {
        val params = snack.view.layoutParams as MarginLayoutParams
        params.setMargins(12, 12, 12, 12)
        snack.view.layoutParams = params
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRoundBordersBg(context: Context, snackbar: Snackbar) {
        snackbar.view.background = context.getDrawable(R.drawable.bg_snackbar)
    }
}