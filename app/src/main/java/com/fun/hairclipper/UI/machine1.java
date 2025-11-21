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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class machine1 extends BaseClass {
    ToggleButton toggleButton;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    private FrameLayout frameLayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        vibrator.cancel();
        mediaPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine1);
        frameLayout = findViewById(R.id.adaptive);
        if (!getPaymentSubscription().isPurchased() && Tool.isNetworkAvailable(this)) {
            loadBannerAD();
        } else {
            frameLayout.setVisibility(View.GONE);
        }
        ImageView home_btn = findViewById(R.id.home_btn);
        home_btn.setOnClickListener(v -> finish());
        mediaPlayer = MediaPlayer.create(machine1.this, R.raw.hair_clipper1);
        toggleButton = findViewById(R.id.toggleButton1);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                vibrator.vibrate(300000);
                mediaPlayer.start();
            } else {
                vibrator.cancel();
                mediaPlayer.stop();
                mediaPlayer.prepareAsync();
            }
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            vibrator.cancel();
            toggleButton.setChecked(false);
        });
    }

    private void loadBannerAD() {
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.BannerAd));
        frameLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }
}