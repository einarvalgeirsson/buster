package com.einarvalgeir.bussrapport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.einarvalgeir.bussrapport.util.SharedPrefsUtil;


public class SplashActivity extends AppCompatActivity {

    private static final int WAIT_TIME_BEFORE_STARTING_APP_MILLIS = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        waitUntilStartingApp();
        determineWhichActivityToStart();
    }

    private void waitUntilStartingApp() {
        new Handler().postDelayed(() -> {
        }, WAIT_TIME_BEFORE_STARTING_APP_MILLIS);
    }

    private void determineWhichActivityToStart() {
        SharedPrefsUtil prefs = new SharedPrefsUtil(getApplicationContext());

        if (prefs.getAssigneeEmail().isEmpty() || prefs.getReporterName().isEmpty()) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
