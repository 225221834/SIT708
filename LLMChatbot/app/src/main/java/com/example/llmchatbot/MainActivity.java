package com.example.llmchatbot;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

     // Initializes the startup screen, which enables setting up of user input
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Adjusts the main view padding to account for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText usernameInput = findViewById(R.id.username);
        Button loginButton = findViewById(R.id.btnLogin);
        
        // Listens for the login button click. Validates the username and navigates
        // to the ChatActivity, passing the name as an extra.
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (!username.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}