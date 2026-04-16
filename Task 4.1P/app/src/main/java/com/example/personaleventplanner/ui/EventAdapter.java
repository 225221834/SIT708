package com.example.personaleventplanner.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personaleventplanner.R;
import com.example.personaleventplanner.model.Event;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// EventAdapter class to display a list of events in a RecyclerView using the EventViewHolder class
// and the OnEventClickListener interface to handle click events on the edit and delete buttons for each event
// The adapter is responsible for creating and binding the views for each item in the list of events to the data
// and setting up the click listeners for the edit and delete buttons to call the appropriate methods on the listener when clicked

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    // List of events to display in the RecyclerView
    private List<Event> events = new ArrayList<>();
    private OnEventClickListener listener;
    // SimpleDateFormat to format the date and time
    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnEventClickListener {
        void onEditClick(Event event);
        void onDeleteClick(Event event);
    }

    // Set the listener for the edit and delete buttons
    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    // Creates a new EventViewHolder for each item in the list of events
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }
    // Binds the data for each event to the views in the EventViewHolder
    // and sets up the click listeners for the edit and delete buttons
    // to call the appropriate methods on the listener when clicked
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = events.get(position);
        holder.textViewTitle.setText(currentEvent.getTitle());
        holder.textViewCategory.setText(currentEvent.getCategory());
        holder.textViewLocation.setText(currentEvent.getLocation());
        holder.textViewDateTime.setText(dateTimeFormatter.format(currentEvent.getDateTime()));

        // Set up click listeners for the edit buttons
        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(currentEvent);
            }
        });
        // Set up click listeners for delete buttons
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(currentEvent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewCategory;
        private final TextView textViewLocation;
        private final TextView textViewDateTime;
        private final ImageButton buttonEdit;
        private final ImageButton buttonDelete;

        // ViewHolder class to hold the views for each item in the RecyclerView list of events
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewCategory = itemView.findViewById(R.id.text_view_category);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
            textViewDateTime = itemView.findViewById(R.id.text_view_date_time);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
