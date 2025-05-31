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
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private ImageButton  googleBtn;
    private AuthRepository authRepo;
    private TextView signupText;
    private TextView forgotPasswordText;
    CallbackManager callbackManager = CallbackManager.Factory.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepo = new AuthRepository(this);

        emailInput = findViewById(R.id.get_email_login);
        passwordInput = findViewById(R.id.get_pass_login);
        loginBtn = findViewById(R.id.loginBtn);
        googleBtn = findViewById(R.id.btnGoogleLogin);
        signupText= findViewById(R.id.signup);
        forgotPasswordText = findViewById(R.id.forgot_password);

        authRepo.checkCurrentUser(new AuthRepository.EmailSignInCallback() {
            @Override
            public void onSignInSuccess(FirebaseUser user) {
                goToMainActivity();
            }

            @Override
            public void onSignInFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
//link with forgoot password screen
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Email login
        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                authRepo.signInWithEmailPassword(email, password, new AuthRepository.EmailSignInCallback() {
                    @Override
                    public void onSignInSuccess(FirebaseUser user) {
                        // 👇 Fetch the user info from DB using UID
                        new UserRepository().fetchUser(user.getUid(), new UserRepository.OnUserFetchListener() {
                            @Override
                            public void onUserFetched(UserModel userModel) {
                                showToast("Welcome, " + userModel.getName());
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
                        showToast("Incorrect Email or Password");
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
                    new UserRepository().fetchUser(user.getUid(), new UserRepository.OnUserFetchListener() {
                        @Override
                        public void onUserFetched(UserModel userModel) {
                            showToast("Welcome, " + userModel.getName());
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
                    showToast("Google sign-in failed: " + errorMessage);
                }

                @Override
                public void onCredentialResponse(GetCredentialResponse credentialResponse) {
                    // You can handle this if needed
                }

                @Override
                public void onCredentialError(String error) {
                    showToast("Google sign-in error: " + error);
                }
            });

        });

        //redirect to signup page
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
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