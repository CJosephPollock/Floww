package com.example.iguest.flowww;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ToggleButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    public final String TAG = "AddActivity";
    private Firebase ref;
    public String newKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://flowww.firebaseio.com/");

        setContentView(R.layout.activity_add);

    }

    public void uploadNewWaterSource(View v) {
        EditText nameInput = (EditText) findViewById(R.id.set_name);
        final String name = nameInput.getText().toString();
        EditText locationDescriptionInput = (EditText) findViewById(R.id.set_location_description);
        final String locationDescription = locationDescriptionInput.getText().toString();
        RatingBar starsInput = (RatingBar) findViewById(R.id.set_rating);

        final float stars = starsInput.getRating();
        final boolean status = ((ToggleButton) findViewById(R.id.set_toggle)).isChecked();
        final String review = ((EditText) findViewById(R.id.set_init_review)).getText().toString();


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double lng = location.getLongitude();
        final double lat = location.getLatitude();


        Review initial = new Review(stars, review, System.currentTimeMillis()/1000);
        FountainLocation fl = new FountainLocation(name, locationDescription, status, initial, lat, lng);

        ref.push().setValue(fl);


        ref.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String recentAddKey = dataSnapshot.getKey();
                Log.v("TEST   ", recentAddKey);
                newKey = recentAddKey;
                Log.v("CAMELBAK   ", newKey);

                Bundle bundle = new Bundle();
//                bundle.putString("name", name);
//                bundle.putString("locationDescription", locationDescription);
//                bundle.putInt("stars", stars);
//                bundle.putBoolean("status", status);
                bundle.putString("lastKey", newKey);
//                bundle.putDouble("lat", lat);
//                bundle.putDouble("lng", lng);
                Log.v("CHECK KEY", ""+ newKey);

                Intent intent = new Intent(AddActivity.this, DetailsView.class);
                intent.putExtras(bundle); // not sure what string to use here...
                startActivity(intent);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }






}



