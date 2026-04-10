package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends BaseActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 5);
        //receives username
        String name = getIntent().getStringExtra("user_name");

        TextView resultText = findViewById(R.id.result_text);
        resultText.setText("Hi " + name + "!\n\nYour Score is\n" + score + " / " + total);

        Button newQuizBtn = findViewById(R.id.new_quiz_button);
        Button finishBtn = findViewById(R.id.finish_button);

        //new quiz button - return to main activity
        newQuizBtn.setOnClickListener(v -> {
            //username is passed on to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_name", name);
            startActivity(intent);
            finish();
        });
        //application is completely closed
        finishBtn.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}