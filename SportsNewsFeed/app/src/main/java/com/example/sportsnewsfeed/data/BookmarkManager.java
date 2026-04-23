package com.example.sportsnewsfeed.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.sportsnewsfeed.NewsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
   Manages the persistence of news item bookmarks using SharedPreferences.
   News items are stored and retrieved as JSON strings using Gson.
 */
public class BookmarkManager {
    private static final String PREF_NAME = "bookmarks";
    private static final String KEY_BOOKMARKS = "bookmark_list";
    SharedPreferences sharedPreferences;// To store and retrieve data from SharedPreferences
    private final Gson gson = new Gson(); // For converting objects to JSON


    public BookmarkManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /*
       Toggles the bookmark status of a news item.
       If already bookmarked, it's removed; otherwise, it's added.
       News item to bookmark or un-bookmark.
     */
    public void toggleBookmark(NewsItem item) {
        List<NewsItem> bookmarks = getBookmarks();
        if (bookmarks.stream().anyMatch(b -> b.getId().equals(item.getId()))) {
            bookmarks.removeIf(b -> b.getId().equals(item.getId()));
        } else {
            bookmarks.add(item);
        }
        saveBookmarks(bookmarks);
    }

    /*
      Checks if a news item is currently bookmarked.
      Uses unique ID of the news item to check.
      True if bookmarked, false otherwise.
     */
    public boolean isBookmarked(String itemId) {
        // Check if item is in bookmarks list
        return getBookmarks().stream().anyMatch(b -> b.getId().equals(itemId));
    }

    /*
       Retrieves the list of bookmarked news items from persistent storage.
       list containing all bookmarked objects.
     */
    public List<NewsItem> getBookmarks() {
        // Get JSON from SharedPreferences with key KEY_BOOKMARKS or return empty list if not found
        String json = sharedPreferences.getString(KEY_BOOKMARKS, null);
        if (json == null || json.isEmpty()) return new ArrayList<>();
        // Convert JSON to list of NewsItem objects
        Type type = new TypeToken<List<NewsItem>>(){}.getType();
        List<NewsItem> bookmarks = gson.fromJson(json, type);
        return bookmarks != null ? bookmarks : new ArrayList<>();
    }

    private void saveBookmarks(List<NewsItem> list) {
        String json = gson.toJson(list); // Convert list to JSON
        // Save JSON to SharedPreferences with key KEY_BOOKMARKS and apply changes immediately
        sharedPreferences.edit().putString(KEY_BOOKMARKS, json).apply();
    }
}