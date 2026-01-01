package com.example.e_rental;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Sudah login → ke MainActivity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // Belum login → ke halaman login
            startActivity(new Intent(SplashActivity.this, halamanku.class));
        }

        finish();
    }
}
