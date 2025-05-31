package com.example.coffeeshop.Repository;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;

import com.example.coffeeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private static final String TYPE_GOOGLE_ID_TOKEN_CREDENTIAL = "com.google.android.libraries.identity.googleid.GOOGLE_ID_TOKEN_CREDENTIAL";
    private final FirebaseAuth mAuth;
    private final Context context;
    private final Executor executor = Executors.newSingleThreadExecutor();

    // Callback interfaces
    public interface EmailSignInCallback {
        void onSignInSuccess(FirebaseUser user);
        void onSignInFailure(String errorMessage);
    }

    public interface GoogleSignInCallback {
        void onSignInSuccess(FirebaseUser user);
        void onSignInFailure(String errorMessage);
        void onCredentialResponse(GetCredentialResponse credentialResponse);
        void onCredentialError(String error);
    }


    public AuthRepository(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();

    }
    public interface PasswordResetCallback {
        void onResetEmailSent();
        void onResetFailed(String error);
    }

    // Email Authentication Methods
    // Email Signup Method
    public void signUpWithEmailPassword(String email, String password, EmailSignInCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signUpWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSignInSuccess(user);
                        } else {
                            Log.w(TAG, "signUpWithEmail:failure", task.getException());
                            String errorMessage = "Signup failed";
                            if (task.getException() != null) {
                                errorMessage += ": " + task.getException().getMessage();
                            }
                            callback.onSignInFailure(errorMessage);
                        }
                    }
                });
    }

    //Email Sign In Method
    public void signInWithEmailPassword(String email, String password, EmailSignInCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSignInSuccess(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            String errorMessage = "Authentication failed";
                            if (task.getException() != null) {
                                errorMessage += ": " + task.getException().getMessage();
                            }
                            callback.onSignInFailure(errorMessage);
                        }
                    }
                });
    }

    // Google Authentication Methods
    public void signInWithGoogle(GoogleSignInCallback callback) {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)  // <-- Allow new accounts too
                .setAutoSelectEnabled(false)           // <-- Let the user pick the account manually
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        androidx.credentials.CredentialManager credentialManager =
                androidx.credentials.CredentialManager.create(context);

        credentialManager.getCredentialAsync(
                context,
                request,
                null,
                executor,
                new androidx.credentials.CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleCredential(result, callback);
                        callback.onCredentialResponse(result);
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        String errorMsg = "Google sign-in failed";
                        if (e instanceof NoCredentialException) {
                            errorMsg = "No credential found";
                        }
                        Log.e(TAG, errorMsg, e);
                        callback.onCredentialError(errorMsg + ": " + e.getMessage());
                    }
                }
        );
    }

    private void handleCredential(GetCredentialResponse credentialResponse, GoogleSignInCallback callback) {
        Credential credential = credentialResponse.getCredential();

        Log.d(TAG, "Credential type: " + credential.getType());
        Log.d(TAG, "Credential class: " + credential.getClass().getName());

        if (credential instanceof GoogleIdTokenCredential) {
            GoogleIdTokenCredential googleCredential = (GoogleIdTokenCredential) credential;
            String idToken = googleCredential.getIdToken();
            Log.d(TAG, "ID Token: " + idToken);
            firebaseAuthWithGoogle(idToken, callback);
        } else {
            Log.e(TAG, "Unexpected credential type or class: " + credential.getClass().getName());
            callback.onSignInFailure("Unexpected credential class");
        }
    }


    private void firebaseAuthWithGoogle(String idToken, GoogleSignInCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSignInSuccess(user);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        callback.onSignInFailure(task.getException() != null ?
                                task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    //Forgot Password
    public void sendPasswordReset(String email, PasswordResetCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResetEmailSent();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        callback.onResetFailed(error);
                    }
                });
    }



    // Common Methods
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
        Log.d(TAG, "User signed out from Firebase");
    }

    public void checkCurrentUser(EmailSignInCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            callback.onSignInSuccess(currentUser);
        }
    }

}