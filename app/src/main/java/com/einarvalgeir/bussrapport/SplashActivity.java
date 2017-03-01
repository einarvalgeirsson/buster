package com.einarvalgeir.bussrapport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    private static final int WAIT_TIME_BEFORE_STARTING_APP_MILLIS = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        waitUntilStartingApp();
    }

    private void waitUntilStartingApp() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }, WAIT_TIME_BEFORE_STARTING_APP_MILLIS);
    }
}
