package com.`fun`.hairclipper.admobHelper

import com.google.android.gms.ads.AdError

open class FullScreenAdListener {

    open fun onAdClicked() {}

    open fun onAdDismissedFullScreenContent() {}

    open fun onAdFailedToShowFullScreenContent(var1: AdError) {}

    open fun onAdImpression() {}

    open fun onAdShowedFullScreenContent() {}

    open fun gotoNext(){}
}