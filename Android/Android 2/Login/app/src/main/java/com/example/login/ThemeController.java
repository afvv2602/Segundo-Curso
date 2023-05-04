package com.example.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeController {
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String THEME_PREF_KEY = "theme_pref";

    public static void applySavedTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt(THEME_PREF_KEY, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    public static void switchTheme(Context context) {
        int currentTheme = AppCompatDelegate.getDefaultNightMode();
        int newTheme = currentTheme == AppCompatDelegate.MODE_NIGHT_NO ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;

        // Save the new theme to SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(THEME_PREF_KEY, newTheme);
        editor.apply();

        // Apply the new theme
        AppCompatDelegate.setDefaultNightMode(newTheme);
    }
}
