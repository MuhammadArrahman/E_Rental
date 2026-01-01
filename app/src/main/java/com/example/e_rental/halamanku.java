package com.example.e_rental;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_rental.R;
import androidx.appcompat.widget.AppCompatButton;

public class halamanku extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanku); // Pastikan ini nama XML halaman 1 Anda

        AppCompatButton btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(halamanku.this, halaman2.class);
            startActivity(intent);
        });
    }
}