package com.example.llmchatbot;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.llmchatbot.database.ChatDatabase;
import com.example.llmchatbot.database.Message;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private MessageAdapter adapter;
    private EditText editMessage;
    private ChatDatabase database;
    private GeminiService geminiService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Gemini API Key - add your key
    private static final String API_KEY = "API_KEY";

     // Initializes the activity, sets up the UI components, handles window insets for correct
     // layout positioning above system bars, and observes database changes to update the chat list.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adjusts the input layout padding dynamically to stay above system bottom navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            findViewById(R.id.input_layout).setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    systemBars.bottom + (int)(8 * getResources().getDisplayMetrics().density)
            );
            return insets;
        });
        //Sets the title of the top action bar
        String username = getIntent().getStringExtra("USERNAME");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gemini AI Chatbot");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        editMessage = findViewById(R.id.edit_message);
        ImageButton btnSend = findViewById(R.id.btn_send);

        adapter = new MessageAdapter();
        if (username != null && !username.isEmpty()) {
            adapter.setUserLetter(username.substring(0, 1).toUpperCase());
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = ChatDatabase.getInstance(this);
        geminiService = new GeminiService(API_KEY);

        // Observes the live data from the database to automatically update the UI when messages are added
        database.messageDao().getAllMessages().observe(this, messages -> {
            adapter.setMessages(messages);
            if (!messages.isEmpty()) {
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });
        //send message button, listens for clicks
        btnSend.setOnClickListener(v -> sendMessage());
    }

     // Captures the user input, then saves it to the Room database, and triggers a request
     // to the Gemini AI service for a response.
    private void sendMessage() {
        String content = editMessage.getText().toString().trim();
        if (!content.isEmpty()) {
            Message userMessage = new Message(content, true, System.currentTimeMillis());
            editMessage.setText("");
            
            executor.execute(() -> {
                // Saves user message to database
                database.messageDao().insert(userMessage);
                
                // Calls Gemini via the service and handles the asynchronous response
                geminiService.generateResponse(content).thenAccept(response -> {
                    Message botMessage = new Message(response, false, System.currentTimeMillis());
                    executor.execute(() -> database.messageDao().insert(botMessage));
                });
            });
        }
    }

    // Cleans up resources to prevent leaks.
    @Override
    protected void onDestroy() {
        super.onDestroy() ;
        executor.shutdown();
    }
}