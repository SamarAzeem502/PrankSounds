package com.`fun`.hairclipper.UI

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.databinding.RateUsDialogBinding
import com.`fun`.hairclipper.helpers.LocaleNow
import com.google.android.gms.ads.AdSize

open class BaseClass : AppCompatActivity() {
    val paymentSubscription by lazy {
        (applicationContext as MyApplication).paymentSubscription
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(this)
        updateAppConfiguration()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })
    }
    private fun setStatusBarGradiant(activity: Activity) {
        val window: Window = activity.window
        val background = ContextCompat.getDrawable(activity, R.drawable.bg_status_bar)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
//        window.navigationBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
        window.setBackgroundDrawable(background)
    }

    private fun updateAppConfiguration() {
        // Disable font scaling
        val fontScale = getResources().configuration.fontScale
        if (fontScale != 1.0f) {
            val configuration = Configuration(getResources().configuration)
            configuration.fontScale = 1.0f
            getResources().updateConfiguration(configuration, getResources().displayMetrics)
        }

        // Disable layout scaling
        val density = getResources().displayMetrics.density
        val scaledDensity = getResources().displayMetrics.scaledDensity
        if (density != scaledDensity) {
            getResources().displayMetrics.scaledDensity = density
            getResources().updateConfiguration(
                getResources().configuration,
                getResources().displayMetrics
            )
        }
    }

    open fun handleBackPressed() {
        finish()
    }

    fun getAdSize(): AdSize {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleNow.wrapContext(newBase))
    }

    fun privacyPolicy() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://docs.google.com/document/d/1eeeGk4RW6Y6XH6d1Uy5faD2gP8_eSmwD3aRpbpd0utU/edit?usp=sharing")
        )
        startActivity(browserIntent)
    }

    fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nLet me recommend you this Amazing application\n\n"
            shareMessage =
                shareMessage + "https://play.google.com/store/apps/details?id=" + packageName + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    open fun rateApp() {
        val rateUsDialogBinding: RateUsDialogBinding = RateUsDialogBinding.inflate(layoutInflater)
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setView(rateUsDialogBinding.root).show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rateUsDialogBinding.btnCancel.setOnClickListener { alertDialog.dismiss() }
        rateUsDialogBinding.imageView2.setOnClickListener { alertDialog.dismiss() }
        rateUsDialogBinding.btnYes.setOnClickListener {
            alertDialog.dismiss()
            val rating: Float = rateUsDialogBinding.ratingBar.rating
            if (rating < 4) {
                appFeedback()
            } else {
                val uri =
                    Uri.parse("market://details?id=$packageName")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }
            }
        }

        rateUsDialogBinding.ratingBar.post {
            object : CountDownTimer(2000L, 300) {
                override fun onTick(millisUntilFinished: Long) {
                    val rating: Float = rateUsDialogBinding.ratingBar.rating
                    rateUsDialogBinding.ratingBar.rating = rating + 1
                }

                override fun onFinish() {
                    rateUsDialogBinding.ratingBar.rating = 0f
                }
            }.start()
        }
    }

    private fun Activity.appFeedback() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("youremail@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

fun Activity.hideBackButtonAndStatusBar() {
    if (Build.VERSION.SDK_INT >= 30) {
        val insetsController = window.insetsController
        insetsController?.let {
            it.hide(WindowInsets.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
