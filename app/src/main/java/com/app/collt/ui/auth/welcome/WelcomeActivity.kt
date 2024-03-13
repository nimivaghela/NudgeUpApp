package com.app.collt.ui.auth.welcome

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.TextAppearanceSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.app.collt.R
import com.app.collt.databinding.ActivityWelcomeBinding
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.helper.IS_FROM_LOGIN
import com.app.collt.helper.IS_FROM_SIGN_UP
import com.app.collt.helper.PRIVACY_POLICY
import com.app.collt.helper.TERMS_AND_CONDITIONS
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.static_page.StaticPageActivity
import com.app.collt.ui.auth.login.LoginActivity

class WelcomeActivity : BaseActivity() {

    override val binding: ActivityWelcomeBinding by binding(R.layout.activity_welcome)

    override fun handleListener() {
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(IS_FROM_LOGIN, true)
            startActivityWithAnimation(intent)
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(IS_FROM_SIGN_UP, true)
            startActivityWithAnimation(intent)
        }

    }

    override fun initObserver() {

    }

    override fun initView() {
        setSpannableString()
    }

    private fun setSpannableString() {
        val spannableString = SpannableStringBuilder()
//        for set regular text style
        spannableString.append(getString(R.string.info_welcome_screen) + " ")
        spannableString.setSpan(
            TextAppearanceSpan(
                this, R.style.TextViewRegularStyle
            ), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        val start: Int = spannableString.length
//        for set bold text style
        spannableString.append(getString(R.string.terms_and_conditions))
        spannableString.setSpan(
            TextAppearanceSpan(this, R.style.TextViewBoldStyle),
            start,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannableString.setSpan(UnderlineSpan(), start, spannableString.length, 0)
        binding.tvTAndC.highlightColor = Color.TRANSPARENT
        binding.tvTAndC.text = spannableString
//        for set text underline & click
        binding.tvTAndC.makeLinks(
            Pair(getString(R.string.terms_and_conditions), View.OnClickListener {
                val intent = Intent(this, StaticPageActivity::class.java)
                intent.putExtra(TERMS_AND_CONDITIONS, true)
                startActivityWithAnimation(intent)
            })
        )

        val spannableString2 = SpannableStringBuilder()
        spannableString2.append(getString(R.string.info_welcome_screen_two) + " ")
        spannableString2.setSpan(
            TextAppearanceSpan(
                this, R.style.TextViewRegularStyle
            ), 0, spannableString2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val start2: Int = spannableString2.length
        spannableString2.append(getString(R.string.privacy_policy))
        spannableString2.setSpan(
            TextAppearanceSpan(this, R.style.TextViewBoldStyle),
            start2,
            spannableString2.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(UnderlineSpan(), start, spannableString2.length, 0)
        binding.tvPrivacyPolicy.highlightColor = Color.TRANSPARENT
        binding.tvPrivacyPolicy.text = spannableString2

        binding.tvPrivacyPolicy.makeLinks(
            Pair(getString(R.string.privacy_policy), View.OnClickListener {
                val intent = Intent(this, StaticPageActivity::class.java)
                intent.putExtra(PRIVACY_POLICY, true)
                startActivityWithAnimation(intent)
            })
        )
    }

    private fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    // use this to change the link color
//                    textPaint.color = textPaint.linkColor
                    // toggle below value to enable/disable
                    // the underline shown below the clickable text
                    textPaint.isUnderlineText = true
                    setTextColor(Color.WHITE)
                    textPaint.bgColor = Color.TRANSPARENT
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.highlightColor = Color.TRANSPARENT
                    view.defaultFocusHighlightEnabled = false
                    view.invalidate()

                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}