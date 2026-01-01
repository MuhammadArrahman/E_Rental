package com.example.e_rental;

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
import com.example.e_rental.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
    // OAUTH CONFIG (ISI DI SINI)
    // =========================
    private static final String TAG = "RegisterOAuth";
    private static final String REDIRECT_URI = "erental://auth";

    // 🔴 ISI DARI FACEBOOK DEVELOPER
    private static final String FACEBOOK_APP_ID = "1374817811002886";

    // 🔴 ISI DARI GITHUB DEVELOPER
    private static final String GITHUB_CLIENT_ID = "Ov23lizSA6CSVi7WadDs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initUI();
        configureGoogleSignIn();

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
    // GOOGLE SIGN IN (FIREBASE)
    // =========================
    private void configureGoogleSignIn() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        // 🔴 default_web_client_id dari strings.xml
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        startActivityForResult(
                googleSignInClient.getSignInIntent(),
                RC_GOOGLE_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                GoogleSignInAccount account =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                                .getResult(ApiException.class);

                handleGoogleSignInResult(account.getIdToken());

            } catch (Exception e) {
                Toast.makeText(this, "Google Sign In gagal", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleGoogleSignInResult(String idToken) {
        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (result.getAdditionalUserInfo().isNewUser()) {
                        saveUserToFirestore(user.getUid(), user.getEmail());
                    }

                    startActivity(new Intent(this, sign_in.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Login Google gagal", Toast.LENGTH_LONG).show()
                );
    }

    // =========================
    // FACEBOOK (CUSTOM TABS)
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
    // GITHUB (CUSTOM TABS)
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
        new CustomTabsIntent.Builder().build()
                .launchUrl(this, Uri.parse(url));
    }

    // =========================
    // REGISTER EMAIL & PASSWORD
    // =========================
    private void attemptRegistration() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(result -> {
                    saveUserToFirestore(
                            result.getUser().getUid(),
                            result.getUser().getEmail()
                    );
                    startActivity(new Intent(this, sign_in.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    // =========================
    // SAVE USER FIRESTORE
    // =========================
    private void saveUserToFirestore(String uid, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("role", "user");
        user.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users").document(uid).set(user);
    }
}
