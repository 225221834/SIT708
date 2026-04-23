package com.example.sportsnewsfeed;
import java.io.Serializable;

public class NewsItem implements Serializable {
    private final String id, title, description, imageUrl, category;
    private final boolean isFeatured;

    public NewsItem(String id, String title, String description, String imageUrl, String category, boolean isFeatured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isFeatured = isFeatured;
    }

    // Getters and setter methods
    public String getId() {
        return id;
    }
    public String getTitle() {

        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getImageUrl() {

        return imageUrl;
    }
    public String getCategory() {
        return category;
    }
    public boolean isFeatured() {
        return isFeatured;
    }
}