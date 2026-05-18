package com.example.lostfoundapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

// MapsActivity displays all lost and found items on a Google Map.
// It features a radius-based search to filter items near the user's current location.

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseItems dbHelper;
    private Spinner spinnerRadius;
    private Location currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize database helper and UI components
        dbHelper = new DatabaseItems(this);
        spinnerRadius = findViewById(R.id.spinner_radius);

        // Client for accessing Google Play services location APIs
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Listener to refresh markers whenever the user changes the search radius
        spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentLocation != null) {
                    updateMarkers();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

     // Manipulates the map once available.
     // This callback is triggered when the map is ready to be used.

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Verify location permissions before enabling My Location layer
        checkLocationPermission();
    }
     // Checks if the app has permission to access the device's location.
     // If not, it requests the necessary permissions from the user.

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        // Permission already granted
        mMap.setMyLocationEnabled(true);
        getCurrentLocation();
    }

     // Retrieves the user's last known location.
     // If location is unavailable, it falls back to a simulated location (Melbourne).

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                currentLocation = location;
            } else {
                // Fallback to a default location (Melbourne) for simulation purposes
                currentLocation = new Location("Simulation");
                currentLocation.setLatitude(-37.8136);
                currentLocation.setLongitude(144.9631);
                Toast.makeText(this, "Using simulated location (Melbourne)", Toast.LENGTH_SHORT).show();
            }
            // Center the camera on the user's location
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            // Load items onto the map
            updateMarkers();
        });
    }
     // Clears the map and adds markers for all items within the selected radius.
     // Marker colors represent item types: Red for Lost, Green for Found.

    private void updateMarkers() {
        if (mMap == null || currentLocation == null) return;

        mMap.clear();
        // Convert radius from Spinner selection (km)
        double radiusKm = Double.parseDouble(spinnerRadius.getSelectedItem().toString());
        List<Items> allItems = dbHelper.getAllItems();

        // If no items exist in database, populate with mock data for demonstration
        if (allItems.isEmpty()) {
            addMockItems();
            allItems = dbHelper.getAllItems();
        }

        for (Items item : allItems) {
            float[] results = new float[1];
            // Calculate distance between user and the item
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                    item.getLatitude(), item.getLongitude(), results);

            float distanceInMeters = results[0];
            // Only add marker if item is within the specified radius
            if (distanceInMeters <= radiusKm * 1000) {
                LatLng itemLatLng = new LatLng(item.getLatitude(), item.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(itemLatLng)
                        .title(item.getName())
                        .snippet(item.getType() + ": " + item.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                item.getType().equalsIgnoreCase("Lost") ? 
                                BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_GREEN)));
            }
        }
    }
     // Generates simulated Lost and Found items around Melbourne city.
     // This is useful for testing features when the database is empty.

    private void addMockItems() {
        double lat = currentLocation.getLatitude();
        double lng = currentLocation.getLongitude();

        // Create representative Lost and Found items near Melbourne landmarks
        dbHelper.insertItem(new Items("Lost", "iPhone 15", "0412345678", "Black case, left at Flinders St", "12/05/2024", "Flinders Street Station", lat + 0.005, lng + 0.005, "Electronics", null, "2024-05-12 10:00:00"));
        dbHelper.insertItem(new Items("Found", "Keys", "0422222222", "Silver ring, found near Library", "13/05/2024", "State Library of Victoria", lat - 0.005, lng + 0.008, "Other", null, "2024-05-13 11:00:00"));
        dbHelper.insertItem(new Items("Lost", "Grey Cat", "0433333333", "Responds to 'Luna'", "14/05/2024", "Carlton Gardens", lat + 0.012, lng - 0.005, "Pets", null, "2024-05-14 12:00:00"));
        dbHelper.insertItem(new Items("Found", "Leather Wallet", "0444444444", "Brown leather, found in tram", "15/05/2024", "Bourke St", lat - 0.01, lng - 0.012, "Wallets", null, "2024-05-15 13:00:00"));
        dbHelper.insertItem(new Items("Lost", "iPad Pro", "0455555555", "In a green bag", "16/05/2024", "Melbourne Central", lat + 0.02, lng + 0.02, "Electronics", null, "2024-05-16 14:00:00"));
    }

     // Handles the result of the location permission request.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, re-run permission check to enable map features
                checkLocationPermission();
            } else {
                // Permission denied, inform user
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
