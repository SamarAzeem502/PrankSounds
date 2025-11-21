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

public class machine13 extends BaseClass {
    ToggleButton toggleButton;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine13);
        frameLayout = findViewById(R.id.adaptive);
        if (!getPaymentSubscription().isPurchased() && Tool.isNetworkAvailable(this)) {
            loadBannerAd();
        } else {
            frameLayout.setVisibility(View.GONE);
        }
        ImageView view = findViewById(R.id.home_btn);
        view.setOnClickListener(v -> finish());
        mediaPlayer = MediaPlayer.create(machine13.this, R.raw.hair_cutter);
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

    private void loadBannerAd() {
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.BannerAd));
        frameLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }
}