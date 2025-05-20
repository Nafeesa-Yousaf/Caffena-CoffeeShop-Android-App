package com.example.coffeeshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.GetCredentialResponse;

import com.example.coffeeshop.R;
import com.example.coffeeshop.Repository.AuthRepository;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private ImageButton  googleBtn,facbookBtn;
    private AuthRepository authRepo;
    CallbackManager callbackManager = CallbackManager.Factory.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize AuthRepository
        authRepo = new AuthRepository(this);

        // Initialize Views
        emailInput = findViewById(R.id.get_email_login);
        passwordInput = findViewById(R.id.get_pass_login);
        loginBtn = findViewById(R.id.loginBtn);
        googleBtn = findViewById(R.id.btnGoogleLogin);
        facbookBtn=findViewById(R.id.btnFacebookLogin);

        // Check if user is already logged in
        authRepo.checkCurrentUser(new AuthRepository.EmailSignInCallback() {
            @Override
            public void onSignInSuccess(FirebaseUser user) {
                goToMainActivity();
            }

            @Override
            public void onSignInFailure(String errorMessage) {
                // No user logged in, continue with normal flow
            }
        });

        // Email login
        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                authRepo.signInWithEmailPassword(email, password, new AuthRepository.EmailSignInCallback() {
                    @Override
                    public void onSignInSuccess(FirebaseUser user) {
                        goToMainActivity();
                    }

                    @Override
                    public void onSignInFailure(String errorMessage) {
                        showToast(errorMessage);
                    }
                });
            } else {
                showToast("Email and Password cannot be empty");
            }
        });

        // Google login
        googleBtn.setOnClickListener(v -> {
            authRepo.signInWithGoogle(new AuthRepository.GoogleSignInCallback() {
                @Override
                public void onSignInSuccess(FirebaseUser user) {
                    goToMainActivity();
                }

                @Override
                public void onSignInFailure(String errorMessage) {
                    showToast("Google sign-in failed: " + errorMessage);
                }

                @Override
                public void onCredentialResponse(GetCredentialResponse credentialResponse) {
                    // Handle credential response if needed
                }

                @Override
                public void onCredentialError(String error) {
                    showToast("Google sign-in error: " + error);
                }
            });
        });

        facbookBtn.setOnClickListener(v -> {
            authRepo.signInWithFacebook(callbackManager, new AuthRepository.FacebookSignInCallback() {
                @Override
                public void onSignInSuccess(FirebaseUser user) {
                    goToMainActivity();
                }

                @Override
                public void onSignInFailure(String errorMessage) {
                    showToast("Facebook sign-in failed: " + errorMessage);
                }

                @Override
                public void onCancel() {
                    showToast("Facebook sign-in cancelled");
                }

                @Override
                public void onError(String errorMessage) {
                    showToast("Facebook sign-in error: " + errorMessage);
                }
            });
        });



    }

    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void showToast(String message) {

        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }


}