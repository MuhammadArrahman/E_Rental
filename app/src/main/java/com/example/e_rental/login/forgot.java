package com.example.e_rental.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.e_rental.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class forgot extends AppCompatActivity {

    private EditText etEmail;
    private AppCompatButton btnSendLink;
    private ImageView ivBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        btnSendLink = findViewById(R.id.btnSendLink);
        ivBack = findViewById(R.id.ivBack);

        // Back ke login
        ivBack.setOnClickListener(v -> {
            startActivity(new Intent(forgot.this, sign_in.class));
            finish();
        });

        // Send reset link
        btnSendLink.setOnClickListener(v -> sendResetLink());
    }

    private void sendResetLink() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Masukkan email terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSendLink.setEnabled(false);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    btnSendLink.setEnabled(true);

                    if (task.isSuccessful()) {

                        // Pindah ke halaman Open Email
                        Intent intent = new Intent(forgot.this, openEmail.class);
                        intent.putExtra("RESET_EMAIL", email);
                        startActivity(intent);
                        finish();

                    } else {
                        Exception e = task.getException();
                        String message;

                        if (e instanceof FirebaseAuthInvalidUserException) {
                            message = "Email tidak terdaftar.";
                        } else {
                            message = "Gagal mengirim email. Coba lagi.";
                        }

                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
