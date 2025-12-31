package com.example.e_rental.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.e_rental.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword, etPhone;
    private Button btnSignUp;
    private ImageView googleLogin, facebookLogin, githubLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 1. Konfigurasi Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // ID ini otomatis ada setelah connect Firebase
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Inisialisasi UI
        initUI();

        // Logika Tombol Social Media
        googleLogin.setOnClickListener(v -> openGooglePicker());
        facebookLogin.setOnClickListener(v -> openCustomTab("https://www.facebook.com/login"));
        githubLogin.setOnClickListener(v -> openCustomTab("https://github.com/login"));

        btnSignUp.setOnClickListener(v -> registerUser());
    }

    private void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);
        googleLogin = findViewById(R.id.googleLogin);
        facebookLogin = findViewById(R.id.facebookLogin);
        githubLogin = findViewById(R.id.githubLogin);
    }

    // --- LOGIKA GOOGLE SIGN IN ---
    private void openGooglePicker() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Toast.makeText(this, "Google Sign In Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        String email = mAuth.getCurrentUser().getEmail();
                        // Simpan ke Firestore jika user baru
                        saveUserData(uid, email, "Belum diatur");
                    }
                });
    }

    // --- LOGIKA CUSTOM TABS (Facebook & GitHub) ---
    private void openCustomTab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // Mengatur warna toolbar agar sesuai dengan tema aplikasi (Biru)
        builder.setToolbarColor(getResources().getColor(R.color.primary_blue));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    // --- LOGIKA REGISTER MANUAL (Email/Pass) ---
    private void registerUser() {
        // ... (Kode register Anda yang sebelumnya tetap sama) ...
    }

    private void saveUserData(String uid, String email, String phone) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("email", email);
        userMap.put("phone", phone);
        userMap.put("role", "user");

        db.collection("users").document(uid).set(userMap)
                .addOnSuccessListener(aVoid -> finish());
    }
}