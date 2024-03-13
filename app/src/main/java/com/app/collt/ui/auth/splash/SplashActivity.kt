package com.app.collt.ui.auth.splash

import android.animation.Animator
import android.content.Intent
import com.app.collt.R
import com.app.collt.databinding.ActivitySplashBinding
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.shared_data.base.BaseActivity
import com.app.collt.ui.auth.home.HomeActivity
import com.app.collt.ui.auth.welcome.WelcomeActivity

class SplashActivity : BaseActivity() {

    override val binding: ActivitySplashBinding by binding(R.layout.activity_splash)


    override fun handleListener() {

    }

    override fun initObserver() {

    }

    override fun initView() {
        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                if (mUserHolder.isLogin) {
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivityWithAnimation(intent)
                } else {
                    val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivityWithAnimation(intent)
                }
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }
        })


    }
}