package com.example.e_rental;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class sign_in extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private AppCompatButton loginButton;
    private TextView signUpLink, forgotPassword;
    private ImageView googleLogin, facebookLogin, githubLogin;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN_GOOGLE = 9001;
    private static final String FACEBOOK_APP_ID = "1229648115673559";
    private static final String GITHUB_CLIENT_ID = "Ov23lismt2iBh1H3VQOO";
    private static final String REDIRECT_URI = "e_rental://auth";
    private static final String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);
        forgotPassword = findViewById(R.id.forgotPassword);

        googleLogin = findViewById(R.id.googleLogin);
        facebookLogin = findViewById(R.id.facebookLogin);
        githubLogin = findViewById(R.id.githubLogin);

        configureGoogleSignIn();

        // Email login
        loginButton.setOnClickListener(v -> loginWithEmail());

        // Link forgot & register
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(sign_in.this, forgot.class)));
        signUpLink.setOnClickListener(v -> startActivity(new Intent(sign_in.this, RegisterActivity.class)));

        // Social login
        googleLogin.setOnClickListener(v -> signInWithGoogle());
        facebookLogin.setOnClickListener(v -> signInWithFacebookCustomTabs());
        githubLogin.setOnClickListener(v -> signInWithGithubCustomTabs());
    }

    private void loginWithEmail() {
        String email = emailInput.getText().toString().trim();
        String pass = passwordInput.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Email & Password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        startActivity(new Intent(sign_in.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(sign_in.this, "Harap verifikasi email terlebih dahulu", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Login gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void handleGoogleSignInResult(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(sign_in.this, "Login dengan Google Berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(sign_in.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.w(TAG, "Google Auth failed", task.getException());
                        Toast.makeText(sign_in.this, "Login Google gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleSignInResult(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed: " + e.getStatusCode());
                Toast.makeText(this, "Google Sign In gagal: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signInWithFacebookCustomTabs() {
        String url = "https://www.facebook.com/v18.0/dialog/oauth"
                + "?client_id=" + FACEBOOK_APP_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code"
                + "&scope=email,public_profile";

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
            Toast.makeText(this, "Membuka Facebook untuk otorisasi...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Login Facebook gagal: " + e.getMessage());
            Toast.makeText(this, "Gagal membuka Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithGithubCustomTabs() {
        String url = "https://github.com/login/oauth/authorize"
                + "?client_id=" + GITHUB_CLIENT_ID
                + "&scope=user:email"
                + "&redirect_uri=" + REDIRECT_URI;

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
            Toast.makeText(this, "Membuka GitHub untuk otorisasi...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Login Github Gagal: " + e.getMessage());
            Toast.makeText(this, "Gagal membuka GitHub", Toast.LENGTH_SHORT).show();
        }
    }

}