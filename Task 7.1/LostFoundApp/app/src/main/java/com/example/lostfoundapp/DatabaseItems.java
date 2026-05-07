package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

 //Helper class to manage the SQLite database for the Lost & Found app.
 //Handles CRUD operations for 'Item' objects.
public class DatabaseItems extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostFoundDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseItems(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
     //Called when the database is created for the first time.
     //Defines the schema for the 'items' table.
    @Override
    public void onCreate(SQLiteDatabase sQdb) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_IMAGE + " BLOB,"
                + COLUMN_TIMESTAMP + " TEXT" + ")";
        sQdb.execSQL(CREATE_ITEMS_TABLE);
    }
     //Called when the database needs to be upgraded like version change
     //Currently drops the old table and recreates a new one.
    @Override
    public void onUpgrade(SQLiteDatabase sQdb, int oldVersion, int newVersion) {
        sQdb.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(sQdb);
    }

     //Inserts a new lost/found item into the database.
     // Returns the row ID of the newly inserted row, or -1 if an error occurred.
    public long insertItem(Items item) {
        SQLiteDatabase sQdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, item.getType());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_PHONE, item.getPhone());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_LOCATION, item.getLocation());
        values.put(COLUMN_CATEGORY, item.getCategory());
        values.put(COLUMN_IMAGE, item.getImage());
        values.put(COLUMN_TIMESTAMP, item.getTimestamp());

        long id = sQdb.insert(TABLE_ITEMS, null, values);
        sQdb.close();
        return id;
    }
     //Retrieves all items from the database.
     //Returns a list of item objects.

    public List<Items> getAllItems() {
        List<Items> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + COLUMN_CATEGORY + " ASC, " + COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase sQdb = this.getReadableDatabase();
        Cursor cursor = sQdb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                item.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                item.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                item.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQdb.close();
        return itemList;
    }

     //Retrieves items filtered by a specific category.
     //Returns a list of Item objects matching the category.
    public List<Items> getItemsByCategory(String category) {
        List<Items> itemList = new ArrayList<>();
        SQLiteDatabase sQdb = this.getReadableDatabase();
        Cursor cursor = sQdb.query(TABLE_ITEMS, null, COLUMN_CATEGORY + "=?", new String[]{category}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                item.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                item.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
                item.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQdb.close();
        return itemList;
    }
     //Deletes a specific item from the database.
     //The id is the unique database ID of the item to delete.
    public void deleteItem(int id) {
        SQLiteDatabase sQdb = this.getWritableDatabase();
        sQdb.delete(TABLE_ITEMS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        sQdb.close();
    }
     //Retrieves a single item by its ID.
     //Returns the item object, or null if not found.
    public Items getItem(int id) {
        SQLiteDatabase sQdb = this.getReadableDatabase();
        Cursor cursor = sQdb.query(TABLE_ITEMS, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Items item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new Items();
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            item.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
            item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
            item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
            item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            item.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
            item.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
            cursor.close();
        }
        sQdb.close();
        return item;
    }
}
