package com.fun.hairclipper.admobHelper;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Ads {
    public static InterstitialAd mInterstitialAd;

    public static void setAds(Context mContext, String adId) {
        if (mInterstitialAd == null) {
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(mContext, adId, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            mInterstitialAd = null;

                        }
                    });
        }


    }

    public static void showAds(Context context) {


        if (mInterstitialAd != null)
            mInterstitialAd.show((Activity) context);
        assert mInterstitialAd != null;
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                mInterstitialAd = null;
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
            }
        });
    }
}