package com.ip.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ip.barcodescanner.service.LocationService;

/*
this is the starting activity of the project
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getGeoLocation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // goto login page
                Intent i = new Intent(SplashScreenActivity.this,
                        LoginActivity.class);

                startActivity(i);
                finish();
            }
        }, 2500);
    }

    private void getGeoLocation() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

}
