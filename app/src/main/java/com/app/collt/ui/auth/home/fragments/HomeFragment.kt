package com.app.collt.ui.auth.home.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.app.collt.R
import com.app.collt.databinding.FragmentHomeBinding
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.helper.THIS_TAG
import com.app.collt.shared_data.base.BaseFragment
import com.app.collt.ui.auth.home.adapter.HomeListDataAdapter
import com.app.collt.ui.auth.sync_contact.ContactListActivity
import com.app.collt.ui.auth.welcome.WelcomeActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getLayoutId() = R.layout.fragment_home

    override fun displayMessage(message: String) {

    }

    override fun initView() {
        setToolBar(
            binding.mToolbar,
            isBackEnable = false,
            isTitleEnable = false,
            isTitleTroubleEnable = false,
            titleText = "",
            titleTrouble = "",
            isSkipVisible = false,
            titleSkipText = "",
            isFriendsVisible = true,
            isLogoVisible = true,
            isAddGroup = true
        )

        binding.apply {
            rvList.adapter = HomeListDataAdapter {

            }
        }
    }

    override fun postInit() {

    }

    override fun initObserver() {

    }

    override fun handleListener() {
        binding.mToolbar.ivAddGroup.setOnClickListener {
            Log.d(THIS_TAG, "handleListener: ivAddGroup Clicked")
            AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setTitle("")
                .setMessage(getString(R.string.are_you_sure_want_to_logout))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        logout()
                    })
                .setNegativeButton(android.R.string.no, null).show()
        }

        binding.mToolbar.tvFriend.setOnClickListener {
            val intent =
                Intent(requireContext(), ContactListActivity::class.java)
            requireActivity().startActivityWithAnimation(intent)
        }
    }

    private fun logout() {
        mUserHolder.clear()
        mUserHolder.setLoginStatus(false)
        startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        requireActivity().finishAffinity()
    }
}