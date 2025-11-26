package com.fun.hairclipper.UI;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fun.hairclipper.R;
import com.fun.hairclipper.tools.Tool;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ChangeTrimmerActivity extends BaseClass {
    private FrameLayout frameLayout;
    private AudioManager audioManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_trimmer);
        frameLayout = findViewById(R.id.adaptivemain);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setVolumeMax();
        if (!getPaymentSubscription().isPurchased() && Tool.isNetworkAvailable(this)) {
            loadCollapsibleBanner();
        } else {
            frameLayout.setVisibility(View.GONE);
        }
        ImageView home = findViewById(R.id.home_btn);
        home.setOnClickListener(view -> {
            finish();
        });
        ImageView machine1 = findViewById(R.id.machine1);
        machine1.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine1.class);
            startActivity(intent);
        });
        ImageView machine2 = findViewById(R.id.machine2);
        machine2.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine2.class);
            startActivity(intent);
        });
        ImageView machine3 = findViewById(R.id.machine3);
        machine3.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine3.class);
            startActivity(intent);
        });
        ImageView machine4 = findViewById(R.id.machine4);
        machine4.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine4.class);
            startActivity(intent);
        });
        ImageView machine5 = findViewById(R.id.machine5);
        machine5.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine5.class);
            startActivity(intent);
        });
        ImageView machine6 = findViewById(R.id.machine6);
        machine6.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine6.class);
            startActivity(intent);
        });
        ImageView machine7 = findViewById(R.id.machine7);
        machine7.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine7.class);
            startActivity(intent);

        });
        ImageView machine8 = findViewById(R.id.machine8);
        machine8.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine8.class);
            startActivity(intent);

        });
        ImageView machine9 = findViewById(R.id.machine9);
        machine9.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine9.class);
            startActivity(intent);

        });
        ImageView machine10 = findViewById(R.id.machine10);
        machine10.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine10.class);
            startActivity(intent);

        });
        ImageView machine11 = findViewById(R.id.machine11);
        machine11.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine11.class);
            startActivity(intent);

        });
        ImageView machine12 = findViewById(R.id.machine12);
        machine12.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine12.class);
            startActivity(intent);

        });
        ImageView machine13 = findViewById(R.id.machine13);
        machine13.setOnClickListener(view -> {
            Intent intent = new Intent(ChangeTrimmerActivity.this, machine13.class);
            startActivity(intent);
        });
    }

    private void loadCollapsibleBanner() {
        Bundle extras = new Bundle();
        extras.putString("collapsible", "top");
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.CollapsibleBannerAd));
        frameLayout.addView(adView);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private void setVolumeMax() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
    }
}