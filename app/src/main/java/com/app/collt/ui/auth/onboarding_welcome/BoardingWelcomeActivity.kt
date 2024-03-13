package com.app.collt.ui.auth.onboarding_welcome

import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.app.collt.R
import com.app.collt.databinding.ActivityOnboardingWelcomeBinding
import com.app.collt.extension.gone
import com.app.collt.extension.visible
import com.app.collt.shared_data.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator

class BoardingWelcomeActivity : BaseActivity() {

    override val binding: ActivityOnboardingWelcomeBinding by binding(R.layout.activity_onboarding_welcome)

    override fun handleListener() {

    }

    override fun initObserver() {

    }

    override fun initView() {
//        mUserHolder.setAfterLoginWhereToNavigate(IS_FROM_WALKTHROUGH)
        val adapter = OnBoardingWelcomeViewPagerAdapter(this@BoardingWelcomeActivity)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    binding.tabLayout.gone()
                } else {
                    binding.tabLayout.visible()
                }
                super.onPageSelected(position)
            }
        })


    }

}