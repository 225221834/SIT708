package com.example.istream.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

 //Entity class representing a video item in a user's playlist.
 //Linked to the User entity via a foreign key on userId.

@Entity(tableName = "playlist",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "userId"))
public class PlaylistItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String youtubeUrl;
    private String title;

    // Getters and Setters
    //Gets the unique ID of the playlist entry.

    public int getId() {
        return id;
    }

    //Sets the unique ID of the playlist entry.
    public void setId(int id) {
        this.id = id;
    }
     //Gets the ID of the user who owns this playlist item.
    public int getUserId() {
        return userId;
    }

    //Sets the ID of the user who owns this playlist item.
    public void setUserId(int userId) {
        this.userId = userId;
    }
    //Gets the YouTube URL of the saved video.
    public String getYoutubeUrl() {
        return youtubeUrl;
    }
     //Sets the YouTube URL of the saved video.
    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    //Gets the title of the video (if extracted).
    public String getTitle() {
        return title;
    }

     //Sets the title of the video.
    public void setTitle(String title) {
        this.title = title;
    }
}