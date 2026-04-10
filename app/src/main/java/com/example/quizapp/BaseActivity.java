package com.example.quizapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public abstract class BaseActivity extends AppCompatActivity {

     String PREFS_NAME = "quiz_prefs";
     String KEY_THEME_MODE = "theme_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int mode = prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO);
        //apply the theme before app is created
        AppCompatDelegate.setDefaultNightMode(mode);
        super.onCreate(savedInstanceState);
    }
    protected void toggleTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //gets the choice of theme and stores it
        int current = prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO);
        int newMode = (current == AppCompatDelegate.MODE_NIGHT_NO)
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO;
        //saves theme choice
        editor.putInt(KEY_THEME_MODE, newMode);
        editor.apply();

        AppCompatDelegate.setDefaultNightMode(newMode);
        recreate();
    }
}