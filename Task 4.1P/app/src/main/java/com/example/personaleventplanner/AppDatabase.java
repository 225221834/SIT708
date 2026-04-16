package com.example.personaleventplanner;

import android.content.Context;
import com.example.personaleventplanner.model.Event;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//@Database tells room this will be the database class
//only 1 table - Event
//exportSchema = false --> disables exporting the DB into JSON file
@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context){
        if(INSTANCE==null){
            synchronized (AppDatabase.class){
                if(INSTANCE==null){
                    // Creates the database instance
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"eventDB").build();
                }
            }
        }
        return INSTANCE;
    }
}
