package com.example.istream.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity class representing a User in the database.
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fullName;
    private String username;
    private String password;

    //Getters and Setters methods
    //Gets the unique identifier for the user.
    public int getId() {
        return id;
    }

    //Sets the ID for the user.
    public void setId(int id) {
        this.id = id;
    }
    //Gets the full name of the user.
    public String getFullName() {
        return fullName;
    }

     //Sets the full name of the user.
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
     //Gets the username used for authentication.
    public String getUsername() {
        return username;
    }

     //Sets the username used for authentication.
    public void setUsername(String username) {
        this.username = username;
    }

    // Gets the password used for authentication.
    public String getPassword() {
        return password;
    }

     //Sets the password used for authentication.
    public void setPassword(String password) {
        this.password = password;
    }
}