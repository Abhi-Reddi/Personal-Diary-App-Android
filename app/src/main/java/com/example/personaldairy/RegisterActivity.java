package com.example.personaldairy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText,confirmPasswordEditText;
    Button registerButton, loginRegButton;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        emailEditText=findViewById(R.id.emailregister);
        passwordEditText=findViewById(R.id.passwordregister);
        confirmPasswordEditText=findViewById(R.id.confirmpasswordregister);
        registerButton=findViewById(R.id.registerbtn);
        loginRegButton=findViewById(R.id.loginregbtn);

        firebaseAuth=FirebaseAuth.getInstance();
//        loginRegButton.setOnClickListener(view -> {
//            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//            finish();
//        });
        loginRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }
    private void registerUser(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword=confirmPasswordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Invalid Email");
            emailEditText.requestFocus();
        }
        if(password.length()<8){
            passwordEditText.setError("Password should be more than 8 characters");
            passwordEditText.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"User Registered Successfully",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"Registration Failed. Try Again",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}