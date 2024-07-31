package com.example.cinemago.activities;

import static com.example.cinemago.Constants.EMAIL;
import static com.example.cinemago.Constants.USERDATA;
import static com.example.cinemago.SharedPreference.userData;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.R;
import com.example.cinemago.SharedPreference;
import com.example.cinemago.adapters.CinemasAdapter;
import com.example.cinemago.adapters.NearbyCinemasAdapter;
import com.example.cinemago.models.Booking;
import com.example.cinemago.models.Cinema;
import com.example.cinemago.utils.SingletonClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This is the main activity where all cinemas has been fetched from the cinema table
 * And shown in recyclerview
 * Also we are checking if user is a admin then Add button is visible
 * He can add cinema
 * Other users can only click on cinema and do bookings
 *
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private RecyclerView recyclerView, recyclerViewnearby;
    private List<Cinema> dataList = new ArrayList<>();
    private List<Cinema> nearbydataList = new ArrayList<>();
    private CinemasAdapter adapter;
    NearbyCinemasAdapter adapternearby;
    SharedPreference sharedPreference;
    private DatabaseReference databaseReference;

    TextView welcomme, name;


    private LocationManager locationManager;
    private LocationListener locationListener;// Example longitude

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreference = new SharedPreference(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        welcomme = findViewById(R.id.welcome);
        name = findViewById(R.id.username);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           sharedPreference.loadUserData();

        /**
         * Checking if user is admin
         */
        if (Objects.equals(userData.email, "teamdatainnovators@cinemago.com"))
           {
               findViewById(R.id.add).setVisibility(View.VISIBLE);
            findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AddCenimaActivity.class));

                }
            });

           }



        if (getIntent().getStringExtra("type").equals("login")) {
            welcomme.setText("Welcome back,");
        } else {
            welcomme.setText("Welcome,");
        }

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView navUseremail = (TextView) headerView.findViewById(R.id.email);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name);
        LinearLayout profile_header = (LinearLayout) headerView.findViewById(R.id.profile_header);

        /**
         * setting email as an logged in user email
         */
        if (userData.email != null) {
            navUseremail.setText(userData.email);
        }

        if (userData.email != null) {
            navUsername.setText(userData.name);
            name.setText(userData.name+"!");
        }



        setupDrawerContent(navigationView);

        recyclerViewnearby = findViewById(R.id.recyclerViewnearby);
        recyclerView = findViewById(R.id.recyclerView);

        /**
         * List passed to recyclerview adapter of cinemas
         */
        recyclerViewnearby.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapternearby = new NearbyCinemasAdapter(this, nearbydataList);
        recyclerViewnearby.setAdapter(adapternearby);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CinemasAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("cinemas");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = 37.8617;
                double longitude = 145.2862;

                // Use the location data
//                calculateDistance(latitude, longitude, targetLat, targetLng);

                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setDistance(calculateDistance(latitude, longitude, Double.parseDouble(dataList.get(i).getLat()), Double.parseDouble(dataList.get(i).getLng())));
                    Log.e("Distances", String.valueOf(calculateDistance(latitude, longitude, Double.parseDouble(dataList.get(i).getLat()), Double.parseDouble(dataList.get(i).getLng()))));
                    if (calculateDistance(latitude, longitude, Double.parseDouble(dataList.get(i).getLat()), Double.parseDouble(dataList.get(i).getLng())) <= 10) {
                        if (!nearbydataList.contains(dataList.get(i))) {
                            nearbydataList.add(dataList.get(i));
                        }
                    }
                }


                adapternearby.notifyDataSetChanged();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        checkPermissionsAndStartLocationUpdates();
        fetchData();

    }

    /**
     * Fetching cinemas table details from firebase and saving in a list
     */
    private void fetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String desricption = snapshot.child("desription").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String lat = snapshot.child("lat").getValue(String.class);
                    String lng = snapshot.child("lng").getValue(String.class);
                    Cinema data = new Cinema(id, name, desricption, image, address, lat, lng);
                    dataList.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load notifications.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.bookings) {
            startActivity(new Intent(MainActivity.this, BookingsActivity.class));
        } else if (menuItem.getItemId() == R.id.logout) {
            sharedPreference.clearToken();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }


        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    /**
     * Permission checked for location and declared in manifest file
     */

    private void checkPermissionsAndStartLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explain to the user why we need to read the contacts
                // This is asynchronous and will display a dialog
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // Permissions are already granted, start location updates
            getLocation();
        }
    }


    private double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        float[] results = new float[1];
        Location.distanceBetween(startLat, startLng, endLat, endLng, results);
        return results[0] / 1000; // returns the distance in kilometers
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, start location updates
                getLocation();
            } else {
                // Permission denied, disable the functionality that depends on this permission.
                // TODO: Show a message to explain why the app needs this permission
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Either GPS_PROVIDER or NETWORK_PROVIDER can be used
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }


}
