package com.swapnil.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Reference to the ImageView in activity_splash.xml
        ImageView weatherLogo = findViewById(R.id.weather_logo);

        // Load the fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        weatherLogo.startAnimation(fadeIn);  // Apply the animation

        // Delay for 3 seconds, then start MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close SplashActivity
            }
        }, 3000); // 3-second delay
    }
}
