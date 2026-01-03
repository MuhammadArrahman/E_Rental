package com.example.e_rental;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// IMPORT PENTING: Sesuaikan dengan letak folder halamanku
import com.example.e_rental.halamanku;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengambil status login dari Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Sudah login → ke MainActivity (di package utama)
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // Belum login → ke halamanku (di package login)
            startActivity(new Intent(SplashActivity.this, halamanku.class));
        }

        // Menutup SplashActivity agar tidak bisa di-"Back" oleh pengguna
        finish();
    }
}