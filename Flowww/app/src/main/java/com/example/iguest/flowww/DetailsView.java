package com.example.iguest.flowww;

import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Firebase.setAndroidContext(this);
        //ref = new Firebase("https://flowww.firebaseio.com/" + getIntent().getExtras().getString("n mk,"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        TextView detailSourceName = (TextView)findViewById(R.id.txtDetailsSourceName);
        ImageView statusIcon = (ImageView)findViewById(R.id.statusIcon);
        TextView detailsSourceLocation = (TextView)findViewById(R.id.txtDetailsSourceLocation);
        RatingBar overallRating = (RatingBar)findViewById(R.id.rtgDetailsSourceStars);
        ListView reviews = (ListView)findViewById(R.id.listDetailsSourceReviews);


        System.out.println("################    " + getIntent().getExtras().getString("lastKey"));


        detailSourceName.setText(getIntent().getExtras().getString("name"));
        detailsSourceLocation.setText(getIntent().getExtras().getString("locationDescription"));

        if(getIntent().getExtras().getBoolean("status")) {
            System.out.println("true");
            //statusIcon.setImageBitmap();
        } else {
            System.out.println("false");
            //statusIcon.setImageBitmap();
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}