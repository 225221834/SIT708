package com.example.personaleventplanner;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.personaleventplanner.model.Event;

import java.util.List;

// EventViewModel extends AndroidViewModel to handle the application context
public class EventViewModel extends AndroidViewModel {

    private final EventRepository repository;// Repository for database operations
    private final LiveData<List<Event>> allEvents;// LiveData to observe changes in the data

    // Constructor to initialize the repository and LiveData
    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
    }
    // Methods to interact with the repository
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(Event event) {// Call the repository's insert method

        repository.insert(event);
    }
    // Call the repository's update method
    public void update(Event event) {
        repository.update(event);
    }
    // Call the repository's delete method
    public void delete(Event event) {

        repository.delete(event);
    }
}