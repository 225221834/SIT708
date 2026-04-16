package com.example.personaleventplanner.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.personaleventplanner.EventViewModel;
import com.example.personaleventplanner.R;
import com.example.personaleventplanner.model.Event;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// AddEventFragment class to add or edit an event in the database using the EventViewModel
// The fragment is responsible for creating and binding the views for each item in the list of events to the data
// and setting up the click listeners for the edit and delete buttons to call the appropriate methods on the listener when clicked
public class AddEventFragment extends Fragment {

    private TextInputEditText editTextTitle, editTextCategory, editTextLocation, editTextDate, editTextTime;
    private EventViewModel eventViewModel;
    private Event existingEvent;
    private Calendar calendar = Calendar.getInstance();

    // SimpleDateFormat to format the date and time
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    @Nullable
    @Override
    // Inflates the layout and sets up the views and view model for the fragment to interact with the database for add/edit events
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextCategory = view.findViewById(R.id.edit_text_category);
        editTextLocation = view.findViewById(R.id.edit_text_location);
        editTextDate = view.findViewById(R.id.edit_text_date);
        editTextTime = view.findViewById(R.id.edit_text_time);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonSave = view.findViewById(R.id.saveButton);

        // Set up the EventViewModel to interact with the database
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        // Check if the fragment was launched with an existing event to edit
        if (getArguments() != null) {
            existingEvent = getArguments().getParcelable("event");

            // If an event was passed as an argument, populate the views with the event's data for editing
            if (existingEvent != null) {
                editTextTitle.setText(existingEvent.getTitle());
                editTextCategory.setText(existingEvent.getCategory());
                editTextLocation.setText(existingEvent.getLocation());
                calendar.setTimeInMillis(existingEvent.getDateTime());
                editTextDate.setText(dateFormatter.format(calendar.getTime()));
                editTextTime.setText(timeFormatter.format(calendar.getTime()));
                buttonSave.setText(R.string.save); // Or "Update Event" if you have the resource
            }
        }
        // Set up click listeners for the views to show date and time pickers and save the event
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());
        buttonSave.setOnClickListener(v -> saveEvent());

        return view;
    }
    // Show date and time pickers and save the event
    private void showDatePicker() {
        // Create a new DatePickerDialog and set the current date as the default selection for the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editTextDate.setText(dateFormatter.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Set the minimum date to the current date to prevent selecting a past date for the event
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    // Show time picker and save the event
    private void showTimePicker() {
        // Create a new TimePickerDialog and set the current time as the default selection for the time picker dialog
        new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            editTextTime.setText(timeFormatter.format(calendar.getTime()));
            // Show the date picker after setting the time to avoid confusion between date and time selection in the UI
            // Show the time picker dialog with the current time as the default selection
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }
    // Save the event to the database and navigate back to the event list fragment after saving the event
    private void saveEvent() {
        String title = editTextTitle.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();

        // Check if all fields are filled in before saving the event to the database
        // and show a toast message if not all fields are filled in
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(category) || TextUtils.isEmpty(location) || 
            TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // Convert the date and time to a long value representing the number of milliseconds
        long dateTime = calendar.getTimeInMillis();
        // Check if an existing event was passed as an argument to edit
        // If an existing event was passed, update the existing event with the new data
        // If no existing event was passed, create a new event with the new data and save it to the database
        // Show a toast message indicating whether the event was saved or updated
        if (existingEvent != null) {
            existingEvent.setTitle(title);
            existingEvent.setCategory(category);
            existingEvent.setLocation(location);
            existingEvent.setDateTime(dateTime);
            eventViewModel.update(existingEvent);
            Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show();
        } else {
            Event event = new Event(title, category, location, dateTime);
            eventViewModel.insert(event);
            Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show();
        }

        Navigation.findNavController(requireView()).navigateUp();
    }
}
