package com.`fun`.hairclipper.UI

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.`fun`.hairclipper.adaptor.IntroAdapterFullNative
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.databinding.ActivityIntroBinding

class IntroActivity : BaseClass() {
    val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.screenViewpager.adapter = IntroAdapterFullNative(this)
        binding.screenViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (AdConstants.INTRO_NATIVE_AD != null) {
                    when (position) {
                        0 -> {
                            AdConstants.SHOWING_INTER_AD = false
                        }

                        1 -> {
                            AdConstants.SHOWING_INTER_AD = true
                        }

                        2 -> {
                            AdConstants.SHOWING_INTER_AD = false
                        }
                    }
                } else {
                    AdConstants.SHOWING_INTER_AD = false
                }
            }
        })
    }

    fun next(position: Int) {
        if (position == 1) {
//            fullBatteryPrefs.saveFirstLaunch(true)
            startActivity(Intent(this, NewMainMenu::class.java))
            finish()
        } else {
            binding.screenViewpager.currentItem += 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}