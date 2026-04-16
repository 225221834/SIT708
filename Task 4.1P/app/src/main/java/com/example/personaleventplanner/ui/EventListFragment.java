package com.example.personaleventplanner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personaleventplanner.EventViewModel;
import com.example.personaleventplanner.R;
import com.example.personaleventplanner.model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// EventListFragment class to display a list of events in a RecyclerView using the EventAdapter class
// and the EventViewModel to observe changes in the data and navigate to the AddEventFragment when clicked on the edit button
// The adapter is responsible for creating and binding the views for each item in the list of events to the data
// and setting up the click listeners for the edit and delete buttons to call the appropriate methods on the listener when clicked
// The fragment also sets up the FloatingActionButton to navigate to the AddEventFragment when clicked
// The fragment is responsible for creating and binding the views for each item in the list of events to the data
// and setting up the click listeners for the edit and delete buttons to call the appropriate methods on the listener when clicked
// The fragment also sets up the FloatingActionButton to navigate to the AddEventFragment when clicked
public class EventListFragment extends Fragment {

    private EventViewModel eventViewModel;
    // Inflates the layout and sets up the RecyclerView and FloatingActionButton
    // Sets up the EventViewModel to observe changes in the data
    // Sets up the adapter to display the list of events
    // Sets up the FloatingActionButton to navigate to the AddEventFragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set up RecyclerView and adapter to display the list of events in the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Set up EventViewModel and observe changes in the data
        final EventAdapter adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        // Set up FloatingActionButton to navigate to AddEventFragment when clicked
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.setEvents(events);
        });
        // Set up click listeners for the adapter
        adapter.setOnEventClickListener(new EventAdapter.OnEventClickListener() {
            @Override
            // Navigate to EventDetailFragment with the selected event when clicked
            public void onEditClick(Event event) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("event", event);
                // Navigate to AddEventFragment with the selected event as an argument to edit it in the database using the EventViewModel
                Navigation.findNavController(view).navigate(R.id.eventListFragment_to_addEventFragment, bundle);
            }

            @Override
            // Delete the selected event when clicked on the delete button
            public void onDeleteClick(Event event) {
                eventViewModel.delete(event);
                Toast.makeText(requireContext(), "Event deleted", Toast.LENGTH_SHORT).show();
            }
        });
        // Set up FloatingActionButton to navigate to AddEventFragment when clicked
        FloatingActionButton fab = view.findViewById(R.id.fab_add_event);
        // Navigate to AddEventFragment when FloatingActionButton is clicked
        fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.eventListFragment_to_addEventFragment));

        return view;
    }
}
