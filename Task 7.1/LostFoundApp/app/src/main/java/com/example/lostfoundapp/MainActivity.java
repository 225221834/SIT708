package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

 //The main entry point of the application.
 //Provides navigation to create new items and view existing items.

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Adjust padding to handle system bars such as status bar, navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnNewAdvert = findViewById(R.id.button_advert);
        Button btnShowItems = findViewById(R.id.btn_showItems);

        // Launches the activity to create a new advertisement
        btnNewAdvert.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddAdvertActivity.class);
            startActivity(intent);
        });

        // Launches the activity to show all lost and found items
        btnShowItems.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShowItemsActivity.class);
            startActivity(intent);
        });
    }
}
