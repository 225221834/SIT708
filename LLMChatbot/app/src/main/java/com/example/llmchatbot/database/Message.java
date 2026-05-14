package com.example.llmchatbot.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

 // Represents a single chat message stored in the Room database.
 // Each instance of this class corresponds to a row in the 'messages' table.

@Entity(tableName = "messages")
public class Message {
    // Primary key ID for each message
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    // The actual text content of the message.
    public String content;

    // Flag to distinguish between user sent messages and AI-received messages.
    public boolean isSentByUser;

    // The time the message was created in milliseconds.
    public long timestamp;

     // Constructs a new Message object.
     // True if sent by the user, false if from the AI.
     // The current time in milliseconds.
    public Message(String content, boolean isSentByUser, long timestamp) {
        this.content = content;
        this.isSentByUser = isSentByUser;
        this.timestamp = timestamp;
    }
}