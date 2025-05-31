package com.example.coffeeshop.Repository;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_UID = "uid";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(String uid, String name, String email) {
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.apply(); // Save changes asynchronously
    }

    public String getUid() {
        return sharedPreferences.getString(KEY_UID, null);
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void clearUser() {
        editor.clear();
        editor.apply();
    }
}
