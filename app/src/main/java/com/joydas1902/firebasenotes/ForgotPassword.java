package com.joydas1902.firebasenotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {
    private EditText mforgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Objects.requireNonNull(getSupportActionBar()).hide();
        mforgotPassword = findViewById(R.id.forgot_password);
        Button mrecoverBtn = findViewById(R.id.recover_btn);
        TextView mbackToLogin = findViewById(R.id.back_to_login);
        firebaseAuth = FirebaseAuth.getInstance();

        mbackToLogin.setOnClickListener(view -> startActivity(new Intent(ForgotPassword.this, MainActivity.class)));

        mrecoverBtn.setOnClickListener(view -> {
            String email = mforgotPassword.getText().toString().trim();
            if(email.isEmpty()){
                Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
            }
            else{
                // send recovery mail
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Recovery mail is send to registered email", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong email or account does't exist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}