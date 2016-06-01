package edu.uw.jpollock.flowww;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class DetailsView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Firebase ref;
    Switch isOperational;
    LatLng location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Firebase.setAndroidContext(this);


        final String key = getIntent().getExtras().getString("lastKey");
        ref = new Firebase("https://flowww.firebaseio.com/" + key);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ADD REVIEW BUTTON
        final Button addReviewBtn = (Button)findViewById(R.id.btnAddReview);

        // WATER SOURCE STATUS SWITCH TOGGLE BUTTON
        isOperational = (Switch)findViewById(R.id.available_switch);

        // TOGGLING STATUS BUTTON - IF TOGGLED TO AVAILABLE/IS WORKING UPDATE ISWORKING VALUE IN FIREBASE TO TRUE, ELSE
        // FALSE IF NOT WORKING STATUS
        isOperational.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ref.child("isWorking").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isWorking = Boolean.parseBoolean(dataSnapshot.getValue().toString());

                        Map<String,Object> taskMap = new HashMap<String,Object>();
                        taskMap.put("isWorking", !isWorking);
                        ref.updateChildren(taskMap);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });


        // IF NOT LOGGED IN CANNOT ADD REVIEW, OTHERWISE CAN ADD REVIEW
        if(ref.getAuth() != null) {
            addReviewBtn.setVisibility(View.VISIBLE);
            isOperational.setClickable(true);

        } else {
            addReviewBtn.setVisibility(View.GONE);
            isOperational.setClickable(false);
        }



        final TextView detailSourceName = (TextView)findViewById(R.id.txtDetailsSourceName); // water source name text
        final TextView detailsSourceLocation = (TextView)findViewById(R.id.txtDetailsSourceLocation); // water source location text

        // ADD REVIEW BUTTON CLICKED - GO TO ADD REVIEW ACTIVITY
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsView.this, AddReviewActivity.class);
                Bundle b = new Bundle();
                b.putString("key", key);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


        final Button loadReviewButton = (Button)findViewById(R.id.loadReviewDetail); // load reviews button to see reviews

        // ON CLICK OF LOAD REVIEW BUTTONS - TAKES YOU TO LISTVIEW ACTIVITY OF REVIEWS ADDED TO THAT WATER SOURCE
        loadReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsView.this, ReviewDetail.class);
                Bundle b = new Bundle();
                b.putString("lastKey", key);
                intent.putExtras(b);
                startActivity(intent);
            }
        });



        // GETS DATA OF WATER SOURCE
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue().toString();
                String desc = snapshot.child("locationDescription").getValue().toString();
                double lat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                double lng = Double.parseDouble(snapshot.child("lng").getValue().toString());
                boolean isWorking = Boolean.parseBoolean(snapshot.child("isWorking").getValue().toString());

                detailsSourceLocation.setText(desc);
                location = new LatLng(lat, lng);
                Marker fountain = mMap.addMarker(new MarkerOptions()
                        .position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));

                isOperational.setChecked(isWorking);

                detailSourceName.setText(name);

            }
            @Override public void onCancelled(FirebaseError error) { }
        });

    }

    // BACK STACK ON BACK BUTTON TO MAP ACTIVITY WHEN WATER SOURCE CREATED AND REVIEW ADDED
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // ON RESUME CALCULATES OVERALL RATING
    @Override
    protected void onResume() {
        super.onResume();
        calculateRating();
    }


    // CALCULATES OVERALL RATING AND SHOWS RATING COUNT FROM NUMBER OF REVIEWS FOR WATER SOURCE
    public void calculateRating() {


        ref.child("reviews").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final RatingBar overallRating = (RatingBar)findViewById(R.id.rtgDetailsSourceStars);
                overallRating.setRating(0.0f);

                float totalPoints = 0;
                int numReviews = 0;

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    //create a new review
                    float rating = Float.parseFloat(messageSnapshot.child("rating").getValue().toString());
                    String desc = messageSnapshot.child("desc").getValue().toString();
                    String title = messageSnapshot.child("title").getValue().toString();
                    Long time = Long.parseLong(messageSnapshot.child("timestamp").getValue().toString());

                    Review review = new Review(rating, desc, title, "28388237");

                    totalPoints += rating;
                    numReviews++;

                }

                // DISPLAYS RATING COUNT
                TextView ratingCount = (TextView) findViewById(R.id.txtDetailsRatingCount);
                if(numReviews == 1) {
                    ratingCount.setText("1 review");
                } else {
                    ratingCount.setText(numReviews + " reviews");
                }
                overallRating.setRating( (float) totalPoints / numReviews);

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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


    }
}