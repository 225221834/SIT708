package com.example.lostfoundapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

 //Hosting activity for the NewAdvertFragment.
 //Manages the lifecycle and initial transaction for the fragment.
public class AddAdvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);

        // Load the NewAdvertFragment into the container if it's the first creation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NewAdvertFragment())
                .commit();
        }
    }
}
