package com.example.lostfoundapp;

import java.io.Serializable;

 //Model class representing a Lost or Found item.
 //Implements Serializable to allow passing of item objects between Activities via Intents.
public class Items implements Serializable {
    private int id;
    private String type; // Lost or Found
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private String category;
    private byte[] image;
    private String timestamp;

    //Default constructor required
    public Items() {
    }
    public Items(String type, String name, String phone, String description, String date, String location, String category, byte[] image, String timestamp) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.image = image;
        this.timestamp = timestamp;
    }

    // Getters and Setters methods
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
