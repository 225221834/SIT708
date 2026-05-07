package com.example.lostfoundapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

 //Fragment responsible for creating a new Lost or Found item.
 //Handles user input, image selection, and data persistence.
public class NewAdvertFragment extends Fragment {

    private RadioGroup radioGroup;
    private EditText fullName, phone_contact, item_description, date_post, item_location;
    private Spinner spinnerCategory;
    private ImageView imageView;
    private byte[] imageBytes;
    private DatabaseItems dbHelper;

     //Result launcher for the image picker intent.
     //Decodes the selected image, shows a preview, and converts it to a byte array.
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        imageBytes = getBytesFromBitmap(bitmap);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
    );
    //Required empty public constructor for fragment recreation by the android system.
    public NewAdvertFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseItems(getContext());

        // Initialize UI components
        radioGroup = view.findViewById(R.id.radioGroup);
        fullName = view.findViewById(R.id.fullName);
        phone_contact = view.findViewById(R.id.phone_contact);
        item_description = view.findViewById(R.id.item_description);
        date_post = view.findViewById(R.id.date_post);
        item_location = view.findViewById(R.id.item_location);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        Button btn_upload = view.findViewById(R.id.btn_upload);
        Button btnSave = view.findViewById(R.id.btn_save);
        imageView = view.findViewById(R.id.imgView);

        // Setup DatePicker for the date field
        date_post.setFocusable(false);
        date_post.setOnClickListener(v -> showDatePicker());

        // Launch image gallery picker
        btn_upload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Set listener for the Save button
        btnSave.setOnClickListener(v -> saveItem());
    }
     //Displays a date picker dialog and sets the selected date to the date posted field.
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    date_post.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
     //Validates user input and saves the item data to the database.
     //Generates a timestamp and creates an Item object for storage.
    private void saveItem() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getContext(), "Please select Lost or Found", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioButton = getView().findViewById(selectedId);
        String type = radioButton.getText().toString();
        
        String name = fullName.getText().toString().trim();
        String phone = phone_contact.getText().toString().trim();
        String description = item_description.getText().toString().trim();
        String date = date_post.getText().toString().trim();
        String location = item_location.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();


         //Verifies that all fields are filled and an image is uploaded.
         //Shows a Toast message if any input is missing
        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty() || imageBytes == null) {
            Toast.makeText(getContext(), "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate current timestamp for the post
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Items item = new Items(type, name, phone, description, date, location, category, imageBytes, timestamp);
        long id = dbHelper.insertItem(item);

        if (id != -1) {
            Toast.makeText(getContext(), "Item Saved Successfully!", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Error saving item", Toast.LENGTH_SHORT).show();
        }
    }

     //Converts a bitmap to a byte array for storage into SQLite
     //Compresses the image to JPEG format at 50% quality to reduce database size.
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}
