package com.fun.hairclipper.UI;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.fun.hairclipper.R;
import com.fun.hairclipper.tools.Tool;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class machine5 extends BaseClass {
    ToggleButton toggleButton;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    private FrameLayout frameLayout;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        vibrator.cancel();
        mediaPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine5);
        frameLayout = findViewById(R.id.adaptive);
        if (!getPaymentSubscription().isPurchased() && Tool.isNetworkAvailable(this)) {
            loadBannerAd();
        } else {
            frameLayout.setVisibility(View.GONE);
        }
        ImageView view = findViewById(R.id.home_btn);
        view.setOnClickListener(v -> finish());
        mediaPlayer = MediaPlayer.create(machine5.this, R.raw.hair_clipper5);
        toggleButton = findViewById(R.id.toggleButton1);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                vibrator.vibrate(300000);
                mediaPlayer.start();
            } else {
                if (mInterstitialAd != null)
                    mInterstitialAd.show(machine5.this);
                vibrator.cancel();
                mediaPlayer.stop();
                mediaPlayer.prepareAsync();

            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vibrator.cancel();
                toggleButton.setChecked(false);
            }
        });
    }

    private void loadBannerAd() {
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.BannerAd));
        frameLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private void loadCollapsibleBanner() {
        Bundle extras = new Bundle();
        extras.putString("collapsible", "top");
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        frameLayout = findViewById(R.id.adaptive);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.CollapsibleBannerAd));
        frameLayout.addView(adView);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }


}