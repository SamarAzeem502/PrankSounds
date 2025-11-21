package com.fun.hairclipper.tools;

import com.google.android.gms.ads.formats.UnifiedNativeAd;

public interface NativeAdLoadListener {

    void onNativeAdLoaded(UnifiedNativeAd unifiedNativeAd);

    void onNativeAdLoadedFail(int errorCode);
}
