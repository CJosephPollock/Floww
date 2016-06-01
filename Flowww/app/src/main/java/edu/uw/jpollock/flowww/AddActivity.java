package edu.uw.jpollock.flowww;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class AddActivity extends AppCompatActivity {
    public final String TAG = "AddActivity";
    private Firebase ref;
    public String newKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIREBASE UTILIZATION
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://flowww.firebaseio.com/");

        setContentView(R.layout.activity_add);

    }

    // METHOD FOR ADDING NEW WATER SOURCE INFORMATION
    public void uploadNewWaterSource(View v) {
        EditText nameInput = (EditText) findViewById(R.id.set_name); // water source name
        final String name = nameInput.getText().toString();
        EditText locationDescriptionInput = (EditText) findViewById(R.id.set_location_description); // location/desc
        final String locationDescription = locationDescriptionInput.getText().toString();
        RatingBar starsInput = (RatingBar) findViewById(R.id.set_rating); // rating of water source

        final float stars = starsInput.getRating(); // rating stars
        final boolean status = ((Switch) findViewById(R.id.available_switch)).isChecked(); // water status - is working(?)
        final String reviewDesc = ((EditText) findViewById(R.id.set_init_review)).getText().toString(); // review description
        final String reviewTitle = ((EditText) findViewById(R.id.init_review_title)).getText().toString(); // title/subject line of review

        // TO GET LOCATION - LONGITUDE AND LATITUDE OF CURRENT LOCATION WATER SOURCE
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double lng = location.getLongitude();
        final double lat = location.getLatitude();

        // INSTANTIATE INITIAL WATER REVIEW FOR NEW WATER SOURCE
        Review initial = new Review(stars, reviewDesc, reviewTitle, System.currentTimeMillis()/1000);
        // INSTANTIATE WATER SOURCE LOCATION
        FountainLocation fl = new FountainLocation(name, locationDescription, status, initial, lat, lng);

        // ADD WATER SOURCE LOCATION INFORMATION TO FIREBASE
        ref.push().setValue(fl);


        // GET MOST RECENTLY ADD WATER SOURCE FROM FIREBASE
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
                intent.putExtras(bundle);
                Toast.makeText(getApplicationContext(), "Fountain Added", Toast.LENGTH_SHORT).show();
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



