package com.example.istream.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

 //Data Access Object (DAO) for managing database operations for Users and Playlist Items.
@Dao
public interface UserDao {

    //Inserts a new user into the database.
    @Insert
    long insertUser(User user);

     //Authenticates a user based on username and password.
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    //Checks if a username already exists in the database.
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User checkUsernameExists(String username);

    //Adds a new video to a user's playlist.
    @Insert
    void insertPlaylistItem(PlaylistItem item);

     //Retrieves all playlist items for a specific user.
    @Query("SELECT * FROM playlist WHERE userId = :userId")
    LiveData<List<PlaylistItem>> getPlaylistForUser(int userId);

     //Deletes a specific item from a user's playlist and the user who owns the item.
    @Query("DELETE FROM playlist WHERE id = :itemId AND userId = :userId")
    void deletePlaylistItem(int itemId, int userId);

    //Checks if a specific URL is already in a user's playlist to prevent duplicates.
    @Query("SELECT * FROM playlist WHERE userId = :userId AND youtubeUrl = :url LIMIT 1")
    PlaylistItem checkUrlExists(int userId, String url);
}