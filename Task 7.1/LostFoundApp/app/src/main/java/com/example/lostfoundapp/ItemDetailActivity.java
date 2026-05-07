package com.example.lostfoundapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


 //Activity that displays the full details of a specific Lost or Found item.
 //Allows users to remove the item from the system.

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView image;
    private TextView type, name, timeStamp, description, location, phone;
    private DatabaseItems dbHelper;
    private Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        image = findViewById(R.id.detail_image);
        type = findViewById(R.id.detail_type);
        name = findViewById(R.id.detail_name);
        timeStamp = findViewById(R.id.detail_timestamp);
        description = findViewById(R.id.detail_description);
        location = findViewById(R.id.detail_location);
        phone = findViewById(R.id.detail_phone);
        Button btnRemove = findViewById(R.id.btn_remove);

        dbHelper = new DatabaseItems(this);

        // Retrieves the item ID passed from the previous activity
        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        if (itemId != -1) {
            item = dbHelper.getItem(itemId);
            if (item != null) {
                displayDetails();
            }
        }

        // Set the listener to delete the item from the database
        btnRemove.setOnClickListener(v -> {
            if (item != null) {
                dbHelper.deleteItem(item.getId());
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after removal
            }
        });
    }

    //Populates the UI components with the item's data details.
    private void displayDetails() {
        type.setText(item.getType());
        name.setText(item.getName());
        timeStamp.setText("Posted on: " + item.getTimestamp());
        description.setText(item.getDescription());
        location.setText("Location: " + item.getLocation());
        phone.setText("Contact: " + item.getPhone());

        // Decodes the byte array image into a Bitmap for display
        if (item.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
            image.setImageBitmap(bitmap);
        }
    }
}
