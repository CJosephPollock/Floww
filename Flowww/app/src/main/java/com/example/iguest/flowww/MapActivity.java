package com.example.iguest.flowww;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleApiClient googleApiClient;
    private GoogleMap map;
    private Marker currentLocationMarker;
    private UiSettings uiSettings;

    public static final String TAG = "MapActivity by TABI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng seattle = new LatLng(47.656146,-122.309663);

        map.setMyLocationEnabled(true);

        currentLocationMarker = map.addMarker(new MarkerOptions()
                .position(seattle)
                .title("Seattle")
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seattle, 13));
        uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        map.setOnMarkerClickListener(this);

    }



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

    public Location getLocation(View v) {
        Location location = null;

        try {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        } catch (SecurityException se) {
            Log.v(TAG, "Error getting location");
        }

        return location;
    }

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

    @Override
    public void onLocationChanged(Location location) {

        if(currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        if(location != null) {
            LatLng change = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocationMarker = map.addMarker(new MarkerOptions().position(change));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(change, 13));
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(currentLocationMarker)) {
            // if click current location marker stuff
            Intent intent = new Intent(MapActivity.this, DetailsView.class);
//            intent.putExtra("EXTRA_TEXT", bundle); // not sure what string to use here...
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_btn:
                Intent intent = new Intent(MapActivity.this, AddActivity.class);
                startActivity(intent);
                return true; //I got this

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}