package com.example.coffeeshop.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshop.R;
import com.example.coffeeshop.Repository.AuthRepository;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordBtn;

    private AuthRepository authRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.get_email_forgot);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);

        authRepo = new AuthRepository(this);

        resetPasswordBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                showToast("Please enter your email");
                return;
            }
            authRepo.sendPasswordReset(email, new AuthRepository.PasswordResetCallback() {
                @Override
                public void onResetEmailSent() {
                    showToast("Password reset email sent! Check your inbox.");
                    finish(); // optional, close this screen after success
                }

                @Override
                public void onResetFailed(String error) {
                    showToast("Failed to send reset email: " + error);
                }
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
