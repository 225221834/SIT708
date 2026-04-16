package com.example.personaleventplanner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personaleventplanner.model.Event;
import java.util.List;

// EventDao interface to define database operations (DAO)
// Room will generate the implementation for these methods for CRUD operations
// The annotations specify the SQL queries to be executed
@Dao
public interface EventDao {
    // CRUD operations
    @Insert
    // Insert a new event into the database
    void insert(Event event);

    @Update
    // Update an existing event in the database
    void update(Event event);

    @Delete
    // Delete an event from the database
    void delete(Event event);

    // Query to retrieve all events ordered by date and time
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    LiveData<List<Event>> getAllEvents();
}