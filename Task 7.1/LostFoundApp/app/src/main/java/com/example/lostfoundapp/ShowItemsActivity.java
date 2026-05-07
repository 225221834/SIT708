package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

 //Activity that displays a list of all Lost & Found items.
 //Includes functionality to filter the list by category.

public class ShowItemsActivity extends AppCompatActivity {

    private Spinner spinnerFilter;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseItems dbHelper;
    private List<Items> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        spinnerFilter = findViewById(R.id.spinner_filter);
        recyclerView = findViewById(R.id.recycler_View_items);
        dbHelper = new DatabaseItems(this);

        setupSpinner();
        setupRecyclerView();
    }

    //Configures the category filter Spinner and its selection listener.
    private void setupSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.filter_categories, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterItems(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //Configures the RecyclerView with a Linear LayoutManager and ItemAdapter.
    private void setupRecyclerView() {
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(itemList, item -> {
            // Open detail screen when an item is clicked
            Intent intent = new Intent(ShowItemsActivity.this, ItemDetailActivity.class);
            intent.putExtra("ITEM_ID", item.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
     //Fetches items from the database based on the selected category and updates the list.
     //The category to filter by
    private void filterItems(String category) {
        if (category == null) return;
        
        if (category.equals("All")) {
            itemList = dbHelper.getAllItems();
        } else {
            itemList = dbHelper.getItemsByCategory(category);
        }
        
        if (itemList != null) {
            adapter.updateList(itemList);
        }
    }

    //Refreshes the list whenever the activity becomes visible
    @Override
    protected void onResume() {
        super.onResume();
        if (spinnerFilter != null && spinnerFilter.getSelectedItem() != null) {
            filterItems(spinnerFilter.getSelectedItem().toString());
        } else {
            filterItems("All");
        }
    }
}
