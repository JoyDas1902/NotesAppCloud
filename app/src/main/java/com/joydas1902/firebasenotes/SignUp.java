package com.joydas1902.firebasenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private EditText msignUpEmail, msignUpPasword;
    private RelativeLayout msignUp;
    private TextView mgoToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        msignUpEmail = findViewById(R.id.signup_email);
        msignUpPasword = findViewById(R.id.signup_password);
        msignUp = findViewById(R.id.signup);
        mgoToLogin = findViewById(R.id.gotologin);

        firebaseAuth = FirebaseAuth.getInstance();

        mgoToLogin.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, MainActivity.class));
        });

        msignUp.setOnClickListener(view -> {
            String email = msignUpEmail.getText().toString().trim();
            String password = msignUpPasword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
            else if(password.length() < 8) {
                Toast.makeText(getApplicationContext(), "Password must be of atleast 8 digits", Toast.LENGTH_SHORT).show();
            }
            else {
                // register the user to FireBase
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        sendEmailVerification();
                    } else{
                        Toast.makeText(getApplicationContext(), "Failed to registered", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
                Toast.makeText(getApplicationContext(), "Verification email is send, Login again", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SignUp.this, MainActivity.class));
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
        }
    }
}