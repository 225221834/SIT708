package com.example.llmchatbot.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

 // The main Room database class for the chat application.
 // Defines the entities and provides access to the DAO.

@Database(entities = {Message.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase instance;

    //Provides access to the Message DAO for database operations.
    public abstract MessageDao messageDao();

    // Singleton pattern to get a single instance of the database across the app.
    // Returns the chat database instance.
    public static synchronized ChatDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChatDatabase.class, "chat_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}