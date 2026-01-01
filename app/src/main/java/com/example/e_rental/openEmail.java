package com.example.e_rental;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_rental.R;

public class openEmail extends AppCompatActivity {

    private AppCompatButton btnOpenEmail;
    private TextView tvSkip, tvTryAnother, tvSubtitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.open_email);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnOpenEmail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // =============================
        // INIT VIEW
        // =============================
        btnOpenEmail = findViewById(R.id.btnOpenEmail);
        tvSkip = findViewById(R.id.tvSkip);
        tvTryAnother = findViewById(R.id.tvTryAnother);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        ivBack = findViewById(R.id.ivBack);

        // =============================
        // AMBIL EMAIL DARI INTENT
        // =============================
        String targetEmail = getIntent().getStringExtra("RESET_EMAIL");
        if (targetEmail != null) {
            tvSubtitle.setText(
                    "We have sent a password recover\ninstructions to " + targetEmail + "."
            );
        }

        // =============================
        // OPEN EMAIL APP
        // =============================
        btnOpenEmail.setOnClickListener(v -> openEmailApp());

        // =============================
        // SKIP -> LOGIN
        // =============================
        tvSkip.setOnClickListener(v -> {
            Intent intent = new Intent(openEmail.this, sign_in.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // =============================
        // TRY ANOTHER EMAIL
        // =============================
        tvTryAnother.setOnClickListener(v -> {
            startActivity(new Intent(openEmail.this, forgot.class));
            finish();
        });

        // =============================
        // BACK BUTTON
        // =============================
        ivBack.setOnClickListener(v -> finish());
    }

    // =============================
    // METHOD OPEN EMAIL APP
    // =============================
    private void openEmailApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://mail.google.com")
            );
            startActivity(intent);
        }
    }
}
