package com.example.coffeeshop.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshop.R;
import com.example.coffeeshop.Repository.UserPreferences;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail;
    private androidx.appcompat.widget.AppCompatButton btnDeleteAccount;
    private UserPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        userPrefs = new UserPreferences(this);

        String name = userPrefs.getName();
        String email = userPrefs.getEmail();
        tvUserName.setText("Name: " + (name != null ? name : "Not found"));
        tvUserEmail.setText("Email: " + (email != null ? email : "Not found"));

        btnDeleteAccount.setOnClickListener(v -> showReauthenticateDialog());
    }

    private void showReauthenticateDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Enter password");

        new AlertDialog.Builder(this).setTitle("Confirm Password").setMessage("Please enter your password to delete your account.").setView(input).setPositiveButton("Confirm", (dialog, which) -> {
            String password = input.getText().toString().trim();
            if (!password.isEmpty()) {
                reauthenticateAndDelete(password);
            } else {
                Toast.makeText(ProfileActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null).show();
    }

    private void reauthenticateAndDelete(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = user.getEmail();
        if (email == null) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        btnDeleteAccount.setEnabled(false);
        btnDeleteAccount.setText("Loading...");

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.delete().addOnCompleteListener(deleteTask -> {
                    btnDeleteAccount.setEnabled(true);
                    btnDeleteAccount.setText("Delete Account");

                    if (deleteTask.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_LONG).show();
                        userPrefs.clearUser();
                        Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to delete account: " + deleteTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                btnDeleteAccount.setEnabled(true);
                btnDeleteAccount.setText("Delete Account");

                Toast.makeText(ProfileActivity.this, "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
