package com.example.istream;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.istream.databinding.ActivityMainBinding;

 //The main activity of the application that hosts the navigation component.
 //It manages the root layout and the navigation controller.

public class MainActivity extends AppCompatActivity {

    private NavController navController;

     //Initializes the activity, sets up view binding, and configures the navigation controller.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewBinding for the activity
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Locate the NavHostFragment to retrieve the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }
     //Handles the behavior of the Up button in the action bar.
    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}