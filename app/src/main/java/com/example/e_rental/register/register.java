package com.example.e_rental.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.example.e_rental.MainActivity;
import com.example.e_rental.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword, etPhone;
    private Button btnSignUp;
    private ImageView googleLogin, facebookLogin, githubLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // =========================
    // GOOGLE SIGN IN
    // =========================
    private GoogleSignInClient googleSignInClient;
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    // =========================
    // OAUTH CONFIG
    // =========================
    private static final String TAG = "RegisterOAuth";
    private static final String REDIRECT_URI = "erental://auth";
    private static final String FACEBOOK_APP_ID = "YOUR_FACEBOOK_APP_ID";
    private static final String GITHUB_CLIENT_ID = "YOUR_GITHUB_CLIENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initUI();
        configureGoogleSignIn();

        // =========================
        // CLICK LISTENER
        // =========================
        btnSignUp.setOnClickListener(v -> attemptRegistration());
        googleLogin.setOnClickListener(v -> signInWithGoogle());
        facebookLogin.setOnClickListener(v -> signInWithFacebookCustomTabs());
        githubLogin.setOnClickListener(v -> signInWithGithubCustomTabs());
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

    // =========================
    // GOOGLE SIGN IN
    // =========================
    private void configureGoogleSignIn() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleSignInResult(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,
                        "Google Sign In gagal",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleGoogleSignInResult(String idToken) {
        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser()) {
                            saveUserToFirestore(user.getUid(), user.getEmail());
                        }

                        Toast.makeText(this,
                                "Login Google berhasil",
                                Toast.LENGTH_LONG).show();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(this,
                                "Login Google gagal",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // =========================
    // FACEBOOK CUSTOM TABS
    // =========================
    private void signInWithFacebookCustomTabs() {
        String url =
                "https://www.facebook.com/v18.0/dialog/oauth"
                        + "?client_id=" + FACEBOOK_APP_ID
                        + "&redirect_uri=" + REDIRECT_URI
                        + "&response_type=code"
                        + "&scope=email,public_profile";

        openCustomTab(url);
    }

    // =========================
    // GITHUB CUSTOM TABS
    // =========================
    private void signInWithGithubCustomTabs() {
        String url =
                "https://github.com/login/oauth/authorize"
                        + "?client_id=" + GITHUB_CLIENT_ID
                        + "&redirect_uri=" + REDIRECT_URI
                        + "&scope=user:email";

        openCustomTab(url);
    }

    private void openCustomTab(String url) {
        try {
            CustomTabsIntent customTabsIntent =
                    new CustomTabsIntent.Builder().build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (Exception e) {
            Log.e(TAG, "Custom Tabs gagal", e);
            Toast.makeText(this,
                    "Gagal membuka browser",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // =========================
    // REGISTER MANUAL EMAIL
    // =========================
    private void attemptRegistration() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this,
                    "Semua field wajib diisi",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(this,
                    "Password tidak sama",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(this,
                    "Password minimal 6 karakter",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    saveUserToFirestore(
                            authResult.getUser().getUid(),
                            authResult.getUser().getEmail()
                    );

                    Toast.makeText(this,
                            "Registrasi berhasil",
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    // =========================
    // FIRESTORE SAVE USER
    // =========================
    private void saveUserToFirestore(String uid, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("role", "user");
        user.put("isVerified", true);
        user.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "User berhasil disimpan ke Firestore"))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Gagal simpan user", e));
    }
}
