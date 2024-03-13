package com.app.collt.ui.auth.home

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.collt.R
import com.app.collt.databinding.ActivityHomeBinding
import com.app.collt.shared_data.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : BaseActivity() {
    override val binding: ActivityHomeBinding by binding(R.layout.activity_home)

    override fun handleListener() {

    }

    override fun initObserver() {
    }

    override fun initView() {
        mUserHolder.setLoginStatus(true)
        setToolBar(
            binding.mToolbar,
            isBackEnable = false,
            isTitleEnable = false,
            isTitleTroubleEnable = true,
            titleText = "",
            titleTrouble = "",
            isSkipVisible = false,
            titleSkipText = "",
            isFriendsVisible = false,
            isAddGroup = false,
            isLogoVisible = false
        )

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.fragmentContainerView)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.notificationFragment,
                R.id.settingFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


}