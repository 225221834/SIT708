package com.example.quizapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
public class QuizActivity extends BaseActivity {
    ViewPager2 viewPager;
    TextView progressText;
    ProgressBar progressBar;
    String userName;
    QuizViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //receives username
        userName = getIntent().getStringExtra("user_name");
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        progressText = findViewById(R.id.progress_text);
        progressBar = findViewById(R.id.progress_bar);
        viewPager = findViewById(R.id.view_pager);
        SwitchCompat themeSwitch = findViewById(R.id.theme_switch);

        // Toggle theme
        boolean isDark = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        themeSwitch.setChecked(isDark);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> toggleTheme());

        QuizPageAdapter adapter = new QuizPageAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false); // Disable swipe

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateProgress(position);
            }
        });

        updateProgress(0);
    }
    public void goToNextQuestion(int currentPosition) {
        if (currentPosition < 4) {
            viewPager.setCurrentItem(currentPosition + 1, true);
        } else {
            // Show results
            int score = viewModel.calculateScore();
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", 5);
            intent.putExtra("user_name", userName);//username is passed on ResultsActivity
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateProgress(int position) {
        //updates the TextView that shows the question counter
        progressText.setText("Question " + (position + 1) + "/5");
        progressBar.setProgress((position + 1) * 20);
    }
}