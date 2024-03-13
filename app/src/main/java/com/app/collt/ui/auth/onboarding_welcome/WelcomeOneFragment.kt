package com.app.collt.ui.auth.onboarding_welcome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.app.collt.R
import com.app.collt.databinding.FragmentWelcomeOneBinding
import com.app.collt.extension.gone
import com.app.collt.extension.startActivityWithAnimation
import com.app.collt.extension.visible
import com.app.collt.helper.BOARDING_POS
import com.app.collt.shared_data.base.BaseFragment
import com.app.collt.ui.auth.home.HomeActivity


class WelcomeOneFragment : BaseFragment<FragmentWelcomeOneBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentWelcomeOneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getLayoutId() = R.layout.fragment_welcome_one

    override fun displayMessage(message: String) {

    }

    override fun initView() {
        binding.apply {
            arguments?.takeIf { it.containsKey(BOARDING_POS) }?.apply {
                when (this.getInt(BOARDING_POS)) {
                    0 -> {
                        tvTitle.text = getString(R.string.lets_get_you_started)
                        tvTitleBlue.text = getString(R.string.started)
                        ivBody.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.onboard_1
                            )
                        )
                        tvInfo.text = getString(R.string.swipe_for_a_quick_intro)
                        btnLetsGo.gone()
                    }

                    1 -> {
                        tvTitle.text = getString(R.string.nudge_your_friends)
                        tvTitleBlue.text = getString(R.string.friends)
                        ivBody.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.onboard_2
                            )
                        )
                        tvInfo.text = getString(R.string.with_fun_prompts)
                        btnLetsGo.gone()
                    }

                    2 -> {
                        tvTitle.text = getString(R.string.choose_how_they_respond)
                        tvTitleBlue.text = getString(R.string.respond)
                        ivBody.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.onboard_3
                            )
                        )
                        tvInfo.text = getString(R.string.they_can_only_see)
                        btnLetsGo.gone()
                    }

                    3 -> {
                        tvTitle.text = getString(R.string.save_the_best_moments_together)
                        tvTitleBlue.text = getString(R.string.together)
                        ivBody.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.onboard_4
                            )
                        )
                        tvInfo.text = getString(R.string.pick_your_friends_best_moments)
                        btnLetsGo.visible()
                    }
                }
            }
        }

    }

    override fun postInit() {

    }

    override fun initObserver() {

    }

    override fun handleListener() {
        binding.apply {
            btnLetsGo.setOnClickListener {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                requireActivity().startActivityWithAnimation(intent)
            }
        }
    }
}