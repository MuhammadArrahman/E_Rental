package com.example.e_rental;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_rental.R;
import androidx.appcompat.widget.AppCompatButton;

public class halaman2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman2); // Sesuaikan nama XML halaman 2

        AppCompatButton btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            startActivity(new Intent(halaman2.this, halaman3.class));
        });
    }
}