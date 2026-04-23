package com.example.sportsnewsfeed;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.sportsnewsfeed.ui.DetailFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    /* Initializes the activity, sets up edge-to-edge display, and configures the bottom navigation.
       Loads the HomeFragment by default.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                // Set padding for the root view to match the system bars
                v.setPadding(systemBars.left, 0, systemBars.right, 0);
                return insets;
            });
        }
        // Bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
        // Handling the navigation item clicks
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Handle navigation item clicks
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_bookmarks) {
                selectedFragment = new BookmarksFragment();
            }
            // Replace the current fragment with the selected fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null)
                        .commit();
            }
            return true;
        });
    }

    /* Navigates to the DetailFragment for a specific news item.
       Adds the transaction to the back stack to allow users to navigate back.
     */
    public void openDetailFragment(NewsItem newsItem) {
        DetailFragment detailFragment = DetailFragment.newInstance(newsItem);
        // Replace current fragment with detail fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}