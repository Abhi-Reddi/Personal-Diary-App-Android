package com.example.diarynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // UI components for user input and actions (email, password, login button, register link)
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView registerLink;

    // Firebase Authentication object to handle user login
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the layout for the login activity
        setContentView(R.layout.activity_login);

        // Initialize UI components
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input for email and password
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                // Validate that both fields are filled
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use Firebase to authenticate user with email and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Display success message and transition to MainActivity after 1 second
                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                new android.os.Handler().postDelayed(() -> {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);  // Start MainActivity
                                    finish();  // Finish LoginActivity to prevent returning to it
                                }, 1000);  // 1-second delay
                            } else {
                                // Display error message if authentication fails
                                Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Set click listener for the register link to redirect to RegisterActivity
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);  // Redirect to the registration screen
        });
    }
}
