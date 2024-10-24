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
        
        // Enable Edge-to-Edge display to allow the app to draw behind system bars
        EdgeToEdge.enable(this);

        // Set the content view to the splash screen layout
        setContentView(R.layout.activity_splash);

        // Use a Handler to delay the transition from the splash screen to the login screen by 3 seconds (3000 milliseconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to navigate from SplashActivity to LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);  // Start LoginActivity
                finish();  // Close the current activity (SplashActivity) so that the user cannot return to it
            }
        }, 3000);  // Delay for 3 seconds before transitioning
    }
}
