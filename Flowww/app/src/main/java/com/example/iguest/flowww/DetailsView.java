package com.example.iguest.flowww;

import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class DetailsView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Firebase ref;
    //create array list of reviews
    private ArrayList reviewsList;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Firebase.setAndroidContext(this);

        String key = getIntent().getExtras().getString("lastKey");
        ref = new Firebase("https://flowww.firebaseio.com/" + key + "/reviews");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        TextView detailSourceName = (TextView)findViewById(R.id.txtDetailsSourceName);
        ImageView statusIcon = (ImageView)findViewById(R.id.statusIcon);
        TextView detailsSourceLocation = (TextView)findViewById(R.id.txtDetailsSourceLocation);
        RatingBar overallRating = (RatingBar)findViewById(R.id.rtgDetailsSourceStars);

        ListView reviews = (ListView)findViewById(R.id.listDetailsSourceReviews);

        reviewsList = new ArrayList<Review>();
        adapter = new ReviewAdapter(this, reviewsList);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Log.v("###", messageSnapshot.child("rating").getValue().toString());
                    Log.v("###", messageSnapshot.child("desc").getValue().toString());
                    //create a new review
                    int rating = Integer.parseInt(messageSnapshot.child("rating").getValue().toString());
                    String desc = messageSnapshot.child("desc").getValue().toString();
                    Review review = new Review(rating, desc);

                    //add it to the arraylist of reviews
                    reviewsList.add(review);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        detailSourceName.setText(getIntent().getExtras().getString("name"));
        detailsSourceLocation.setText(getIntent().getExtras().getString("locationDescription"));






//        ArrayAdapter<Review> arrayAdapter = new ArrayAdapter<Review>(
//                getApplicationContext(),
//                reviewsList
//        );

        Log.v("WTF     ", adapter.toString());
        reviews.setAdapter(adapter);

        if(getIntent().getExtras().getBoolean("status")) {
            System.out.println("true");
            //statusIcon.setImageBitmap();
        } else {
            System.out.println("false");
            //statusIcon.setImageBitmap();
        }


    }

    public class ReviewAdapter extends ArrayAdapter<Review> {
        public ReviewAdapter(Context context, ArrayList<Review> reviews) {
            super(context, 0, reviews);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Get the data for the item in a given position
            Review review = getItem(position);
            //Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
            }
            //Lookup view for data population
            TextView rating = (TextView)convertView.findViewById(R.id.rating_item);
            TextView desc = (TextView)convertView.findViewById(R.id.desc_item);

            Log.v("CHECK ", "" + review.rating);
            Log.v("CHECK 2 ", review.desc);

            //Set the fields for the "row"
            rating.setText("" + review.rating);
            desc.setText(review.desc);



            return convertView;
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