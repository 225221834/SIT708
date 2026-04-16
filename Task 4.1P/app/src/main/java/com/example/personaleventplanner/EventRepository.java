package com.example.personaleventplanner;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.personaleventplanner.model.Event;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// EventRepository class to handle database operations for the Event entity
// The repository is responsible for providing a clean API for accessing and manipulating data in the database
public class EventRepository{

    private final EventDao eventDao;// Access to the DAO (Data Access Object) for database operations
    private final LiveData<List<Event>> allEvents;// LiveData to observe changes in the data

    // ExecutorService to perform database operations asynchronously
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Constructor to initialize the DAO and LiveData
    public EventRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        eventDao = appDatabase.eventDao();
        allEvents = eventDao.getAllEvents();
    }
    // Methods to interact with the database using the DAO
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(Event event) {// Execute the insertion operation asynchronously

        executorService.execute(() -> eventDao.insert(event));
    }

    public void update(Event event) {// Execute the update operation asynchronously


        executorService.execute(() -> eventDao.update(event));
    }

    public void delete(Event event) {// Execute the deletion operation asynchronously

        executorService.execute(() -> eventDao.delete(event));
    }
}