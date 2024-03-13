package com.app.collt.ui.auth.onboarding_welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.collt.helper.BOARDING_POS

class OnBoardingWelcomeViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        val welcomeOneFragment = WelcomeOneFragment()
        welcomeOneFragment.arguments = Bundle().apply {
            putInt(
                BOARDING_POS,
                position
            )
        }

        return welcomeOneFragment
    }


}