package com.`fun`.hairclipper.admobHelper

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatDelegate
import com.`fun`.hairclipper.helpers.AppPrefs
import com.`fun`.hairclipper.helpers.LocaleNow
import com.`fun`.hairclipper.helpers.PaymentSubscription
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics

class MyApplication : Application() {
    val paymentSubscription: PaymentSubscription by lazy { PaymentSubscription(this) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        AppCompatDelegate.setDefaultNightMode(AppPrefs.getAppTheme(this, "app_theme"))
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            AppOpenManager(this)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleNow.wrapContext(base))
    }
}

val analytics by lazy { Firebase.analytics }
fun internetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: Network? = connectivityManager.activeNetwork
    val networkCapabilities: NetworkCapabilities? =
        connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities?.run {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } == true
}