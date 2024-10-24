package com.example.diarynotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diarynotes.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables edge-to-edge content for the activity
        EdgeToEdge.enable(this);

        // Set the content view to the splash screen layout
        setContentView(R.layout.activity_splash);

        // Delay execution of the Runnable by 3 seconds (3000 milliseconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the LoginActivity after the splash screen delay
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                // Finish the current activity so the user cannot return to the splash screen
                finish();
            }
        }, 3000); // 3-second delay before moving to the LoginActivity
    }
}
