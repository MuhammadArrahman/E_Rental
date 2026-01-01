package com.example.e_rental;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_rental.R;
import androidx.appcompat.widget.AppCompatButton;

public class halaman3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman3); // Sesuaikan nama XML halaman 3

        AppCompatButton btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            // Setelah intro selesai, pindah ke halaman Login
            startActivity(new Intent(halaman3.this, sign_in.class));
            finish(); // Agar user tidak bisa kembali ke intro saat menekan tombol back
        });
    }
}