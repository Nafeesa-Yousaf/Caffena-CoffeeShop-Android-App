package com.example.coffeeshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.GetCredentialResponse;

import com.example.coffeeshop.Domain.UserModel;
import com.example.coffeeshop.R;
import com.example.coffeeshop.Repository.AuthRepository;
import com.example.coffeeshop.Repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signupBtn;
    private ImageButton googleSignupBtn;
    private TextView loginRedirect;

    private AuthRepository authRepo;
    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Repositories
        authRepo = new AuthRepository(this);
        userRepo = new UserRepository();

        // Initialize Views
        nameInput = findViewById(R.id.get_name_signup);           // NEW
        emailInput = findViewById(R.id.get_email_signup);
        passwordInput = findViewById(R.id.get_pass_signup);
        confirmPasswordInput = findViewById(R.id.get_confirm_pass_signup);
        signupBtn = findViewById(R.id.signupBtn);
        googleSignupBtn = findViewById(R.id.btnGoogleSignup);
        loginRedirect = findViewById(R.id.login_redirect);

        // Email Sign Up
        signupBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();          // NEW
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("All fields are required");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showToast("Passwords do not match");
                return;
            }

            authRepo.signUpWithEmailPassword(email, password, new AuthRepository.EmailSignInCallback() {
                @Override
                public void onSignInSuccess(FirebaseUser user) {
                    // Store in Realtime Database
                    userRepo.registerUser(name, email, user.getUid());
                    new UserRepository().fetchUser(user.getUid(), SignupActivity.this, new UserRepository.OnUserFetchListener() {
                        @Override
                        public void onUserFetched(UserModel userModel) {
                            showToast("Sign up successful!");
                            goToMainActivity();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            showToast("User data fetch failed: " + errorMessage);
                        }
                    });



                }

                @Override
                public void onSignInFailure(String errorMessage) {
                    showToast("Sign up failed: " + errorMessage);
                }
            });
        });

        // Google Signup (unchanged)
        googleSignupBtn.setOnClickListener(v -> {
            authRepo.signInWithGoogle(new AuthRepository.GoogleSignInCallback() {
                @Override
                public void onSignInSuccess(FirebaseUser user) {
                    userRepo.registerUser(user.getDisplayName(), user.getEmail(), user.getUid());
                    new UserRepository().fetchUser(user.getUid(), SignupActivity.this, new UserRepository.OnUserFetchListener() {
                        @Override
                        public void onUserFetched(UserModel userModel) {
                            showToast("Sign up successful!");
                            goToMainActivity();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            showToast("User data fetch failed: " + errorMessage);
                        }
                    });

                }

                @Override
                public void onSignInFailure(String errorMessage) {
                    showToast("Google sign-up failed: " + errorMessage);
                }

                @Override
                public void onCredentialResponse(GetCredentialResponse credentialResponse) {}

                @Override
                public void onCredentialError(String error) {
                    showToast("Google sign-up error: " + error);
                }
            });
        });

        // Login redirect
        loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void goToMainActivity() {
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
