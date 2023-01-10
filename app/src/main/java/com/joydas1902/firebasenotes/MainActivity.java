package com.joydas1902.firebasenotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private EditText mloginEmail, mloginPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        mloginEmail = findViewById(R.id.login_email);
        mloginPassword = findViewById(R.id.login_password);
        RelativeLayout mlogin = findViewById(R.id.login);
        RelativeLayout mgoToSignUp = findViewById(R.id.gotosignup);
        TextView mgoToForgotPassword = findViewById(R.id.gotofogot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // User is already logged in
        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }

        mgoToSignUp.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignUp.class)));

        mgoToForgotPassword.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ForgotPassword.class)));

        mlogin.setOnClickListener(view -> {
            String email = mloginEmail.getText().toString().trim();
            String password = mloginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                // Login the user
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkEmailVerification();
                    } else {
                        Toast.makeText(getApplicationContext(), "Account does't exist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        if (firebaseUser.isEmailVerified()) {
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}