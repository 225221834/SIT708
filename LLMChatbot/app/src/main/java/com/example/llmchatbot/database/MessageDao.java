package com.example.llmchatbot.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

 // Data Access Object (DAO) for the 'messages' table.
 // It defines the methods used to interact with the database.
@Dao
public interface MessageDao {
     // Retrieves all messages from the database sorted by timestamp.
     // Uses live data to allow the UI to observe changes in real-time.
     // Live data that list all Message entities.
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    LiveData<List<Message>> getAllMessages();

    //Inserts a new message into the database.
    @Insert
    void insert(Message message);

    //Deletes all messages from the messages table.
    @Query("DELETE FROM messages")
    void deleteAll();
}