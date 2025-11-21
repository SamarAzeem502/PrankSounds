package com.`fun`.hairclipper.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.databinding.ActivityThemeSelectionBinding
import com.`fun`.hairclipper.tools.AppPrefs

class ThemeSelectionActivity : BaseClass() {
    private lateinit var binding: ActivityThemeSelectionBinding
    private var currentTheme: Int = AppCompatDelegate.MODE_NIGHT_NO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentTheme = AppPrefs.getAppTheme(this, "app_theme")
        initViews()
    }

    private fun initViews() {
        val lightThemeImgResId = if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.light_theme_img
        } else {
            R.drawable.light_theme_img_selected
        }
        val darkThemeImgResId = if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.dark_theme_img_selected
        } else {
            R.drawable.dark_theme_img
        }
        Glide.with(this).load(lightThemeImgResId).into(binding.appCompatImageView)
        Glide.with(this).load(darkThemeImgResId).into(binding.appCompatImageView2)
        binding.homeBtn.setOnClickListener { onBackPressed() }
        binding.appCompatImageView.setOnClickListener {
            setThemeAndImages(
                AppCompatDelegate.MODE_NIGHT_NO,
                binding.appCompatImageView,
                binding.appCompatImageView2,
                R.drawable.light_theme_img_selected,
                R.drawable.dark_theme_img
            )
        }
        binding.appCompatImageView2.setOnClickListener {
            setThemeAndImages(
                AppCompatDelegate.MODE_NIGHT_YES,
                binding.appCompatImageView,
                binding.appCompatImageView2,
                R.drawable.light_theme_img,
                R.drawable.dark_theme_img_selected
            )
        }
        binding.btnApply.setOnClickListener {
            applyTheme()
        }
    }

    private fun applyTheme() {
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES && paymentSubscription.isPurchased.not()) {
            startActivity(Intent(this, PremiumScreen::class.java))
        } else {
            if (currentTheme != AppPrefs.getAppTheme(this, "app_theme")) {
                AppPrefs.setAppTheme(this, "app_theme", currentTheme)
                AppCompatDelegate.setDefaultNightMode(currentTheme)
                val intent = Intent(this, SplashScreen::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                onBackPressed()
            }
        }
    }

    private fun setThemeAndImages(
        themeMode: Int, imageView1: AppCompatImageView,
        imageView2: AppCompatImageView, imageRes1: Int, imageRes2: Int
    ) {
        currentTheme = themeMode
        imageView1.setImageResource(imageRes1)
        imageView2.setImageResource(imageRes2)
    }
}