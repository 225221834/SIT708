package com.example.personaleventplanner.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Event class to represent an event entity in the database table using Room annotations
// and Parcelable (needed for passing Event between fragments) - implements Parcelable interface
// and implements the writeToParcel and describe contents methods
@Entity(tableName = "events")
// Parcelable (needed for passing Event between fragments) - implements Parcelable interface
// and implements the writeToParcel and describe contents methods
public class Event implements Parcelable {

    // Primary key for the database table
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String category;
    private String location;
    private long dateTime;

    // Required empty constructor for Room
    public Event() {}
    // Constructor to create a new Event object
    public Event(String title, String category, String location, long dateTime) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.dateTime = dateTime;
    }

    // Parcelable (needed for passing Event between fragments)
    // Constructor to create a new Event object from a Parcel
    // Read the fields of the Event object from the Parcel
    protected Event(Parcel in) {
        id = in.readInt();
        title = in.readString();
        category = in.readString();
        location = in.readString();
        dateTime = in.readLong();
    }
    // Creator for creating Event objects from a Parcel - Parcelable (needed for passing Event between fragments)
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        // Create a new Event object from a Parcel
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        // Create a new array of Event objects with the given size
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    // Describe the contents of the Event object
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize the Event object to a Parcel for passing between fragments
    // Write the fields of the Event object to the Parcel
    // The order of writing the fields is important for correct deserialization in the other fragment
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(location);
        dest.writeLong(dateTime);
    }

    // Getters and Setter methods
    public int getId() {
        return id; }
    public void setId(int id) {
        this.id = id; }

    public String getTitle() {
        return title; }
    public void setTitle(String title) {
        this.title = title; }

    public String getCategory() {
        return category; }
    public void setCategory(String category) {
        this.category = category; }

    public String getLocation() {
        return location; }
    public void setLocation(String location) {
        this.location = location; }

    public long getDateTime() {
        return dateTime; }
    public void setDateTime(long dateTime) {
        this.dateTime = dateTime; }
}