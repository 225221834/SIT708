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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

 //Fragment responsible for creating a new Lost or Found item.
 //Handles user input, image selection, and data persistence.
public class NewAdvertFragment extends Fragment {

    private RadioGroup radioGroup;
    private EditText fullName, phone_contact, item_description, date_post;
    private MaterialAutoCompleteTextView item_location;
    private Spinner spinnerCategory;
    private ImageView imageView;
    private byte[] imageBytes;
    private DatabaseItems dbHelper;
    private double latitude, longitude;
    private String selectedAddress;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionsList = new ArrayList<>();

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

    //Inflates the fragment layout.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_advert, container, false);
    }

     // Called after the view is created then initializes places API and UI components,
     // database helper, and sets up listeners for user interaction.

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(requireContext().getApplicationContext(), "AIzaSyCR93Y93JKM1i5oCakKkGpfh2WWWq9Brwg");
        }
        placesClient = Places.createClient(requireContext());

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

        setupAutocomplete();

        // Set listener for the Save button
        btnSave.setOnClickListener(v -> saveItem());
    }

     // Configures the auto-complete text view for location searching.
     // Adds a text listener for fetching predictions and an item click listener to retrieve detailed place information.

    private void setupAutocomplete() {
        item_location.setThreshold(1);
        item_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fetchPredictions(s.toString());
                }
                // Reset coordinates if text is changed manually
                latitude = 0;
                longitude = 0;
                selectedAddress = null;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        item_location.setOnItemClickListener((parent, view, position, id) -> {
            AutocompletePrediction prediction = predictionsList.get(position);
            String placeId = prediction.getPlaceId();
            selectedAddress = prediction.getFullText(null).toString();
            item_location.setText(selectedAddress, false);
            
            fetchPlaceDetails(placeId);
        });
    }

     // Fetches location predictions from the Google places API based on user input.
     // Updates the adapter for the autocomplete text view with the results.

    private void fetchPredictions(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            predictionsList = response.getAutocompletePredictions();
            List<String> suggestions = new ArrayList<>();
            for (AutocompletePrediction prediction : predictionsList) {
                suggestions.add(prediction.getFullText(null).toString());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_dropdown_item_1line, suggestions);
            item_location.setAdapter(adapter);
            
            if (!suggestions.isEmpty()) {
                item_location.showDropDown();
            }
        }).addOnFailureListener(exception -> {
            // Silently assign simulation coordinates in background to avoid repeated toast spam
            latitude = -37.8136 + (Math.random() - 0.5) * 0.05;
            longitude = 144.9631 + (Math.random() - 0.5) * 0.05;
            exception.printStackTrace();
        });
    }
     // Retrieves latitude and longitude coordinates for a specific place selected by the user.
     // The placeId is the unique identifier for the selected place.

    private void fetchPlaceDetails(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place = response.getPlace();
            if (place.getLatLng() != null) {
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
            }
        }).addOnFailureListener(exception -> {
            exception.printStackTrace();
        });
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

        // Check if a location from suggestions was actually picked to have coordinates
        if (latitude == 0 && longitude == 0) {
            // Force Melbourne coordinates if typing manually and API is disabled
            latitude = -37.8136 + (Math.random() - 0.5) * 0.05;
            longitude = 144.9631 + (Math.random() - 0.5) * 0.05;
            Toast.makeText(getContext(), "Manual location: Simulation for Melbourne coordinates.", Toast.LENGTH_SHORT).show();
        }

        // Generate current timestamp for the post
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Items item = new Items(type, name, phone, description, date, location, latitude, longitude, category, imageBytes, timestamp);
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
