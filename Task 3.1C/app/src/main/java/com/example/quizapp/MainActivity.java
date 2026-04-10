package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameInput = findViewById(R.id.name_input);
        Button startButton = findViewById(R.id.start_button);

        // Retains name temporarily if coming back from "Take New Quiz"
        String savedName = getIntent().getStringExtra("user_name");
        if (savedName != null && !savedName.isEmpty()) {
            nameInput.setText(savedName);
        }

        //start button
        startButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            //passing username between activities using intent extra
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("user_name", name);
            startActivity(intent);
        });
    }
}