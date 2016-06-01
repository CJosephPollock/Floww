package com.example.iguest.flowww;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleApiClient googleApiClient;
    private GoogleMap map;
    private Marker currentLocationMarker;
    private UiSettings uiSettings;
    private Firebase ref;
    HashMap<Marker, String> markerList;

    public static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // DISPLAY MAP (FRAGMENT)
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // BUILD GOOGLE API CONNECTION
        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // LIST OF MARKERS ON MAP
        markerList = new HashMap<Marker, String>();

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://flowww.firebaseio.com/");

        // ADD MARKER TO MAP WITH CURRENT LOCATION LATITUDE AND LONGITUDE
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    //Log.v("###", messageSnapshot.child("lat").getValue().toString());
                    //Log.v("###", messageSnapshot.child("lng").getValue().toString());
                    //create a new review
                    double lat = (Double) (messageSnapshot.child("lat").getValue());
                    double lng = (Double) (messageSnapshot.child("lng").getValue());


                    Marker fountain = map.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));

                    String key = messageSnapshot.getKey();
                    markerList.put(fountain, key);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // FLOATING ACTION BUTTON
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.add_resource_button);
        fab.setImageResource(R.drawable.add_water);

        setFABVisibility();

        // ON CLICK OF FLOATING ACTION BUTTON = ADD NEW WATER SOURCE (ADDACTIVITY)
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, AddActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    // IF LOGGED IN CAN ADD NEW WATER SOURCE, IF NOT THEN NOT ABLE TO
    public void setFABVisibility() {
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.add_resource_button);
        if(ref.getAuth() != null) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    // ON START - CONNECT TO GOOGLE MAPS API
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    // ON STOP - DISCONNECT TO GOOGLE MAPS API
    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    // ON MAP LOAD/READY
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // ENABLE CURRENT LOCATION
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        map.setMyLocationEnabled(true);

        // SET VIEW OF MAP TO CURRENT LOCATION
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));

        // ENABLE ZOOM CONTROLS ON MAP
        uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        map.setOnMarkerClickListener(this);

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, MapActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }


    // ON CONNECTED OF GOOGLE MAPS LOCATION
    @Override
    public void onConnected(Bundle bundle) {
        getLocation(null);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        locationRequest.setInterval(10000);

        int checkPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermission == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            getLocation(null);
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1
            );
        }

    }

    // GETS LOCATION
    public Location getLocation(View v) {
        Location location = null;

        try {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        } catch (SecurityException se) {
            Log.v(TAG, "Error getting location");
        }

        return location;
    }

    // PERMISSION REQUEST TO CONNECT
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onConnected(null);
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    // UPDATES MARKER OF LOCATION WHEN NOTICED IT CHANGES
    @Override
    public void onLocationChanged(Location location) {

        if(currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        if(location != null) {
            LatLng change = new LatLng(location.getLatitude(), location.getLongitude());
            //currentLocationMarker = map.addMarker(new MarkerOptions().position(change));
        }

    }

    // WHEN CLICK ON MARKER, TAKES YOU TO DETAILS VIEW OF THAT WATER SOURCE MARKER
    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapActivity.this, DetailsView.class);
        Bundle b = new Bundle();
        b.putString("lastKey", markerList.get(marker));
        intent.putExtras(b); // not sure what string to use here...
        startActivity(intent);
        return true;
    }

    // MENU WITH LOGIN/LOGOUT TOGGLE BUTTON DEPENDING ON WHETHER YOU ARE LOGGED IN OR NOT
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.changeAuth);
        if(ref.getAuth() != null) {
            item.setTitle("Log out");
        } else {
            item.setTitle("Log in");
        }

        return true;
    }

    // IF LOGGED IN, GIVES YOU LOG OUT BUTTON AND ALLOWS YOU TO SEE FAB BUTTON, OTHERWISE SHOWS LOG IN BUTTON WHICH TAKES
    // YOU TO LOGIN ACTIVITY
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.changeAuth:
                if(ref.getAuth() != null) {
                    ref.unauth();
                    item.setTitle("Log in");
                } else {
                    Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                setFABVisibility();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}