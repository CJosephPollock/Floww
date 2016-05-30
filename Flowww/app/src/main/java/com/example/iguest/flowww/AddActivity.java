package com.example.iguest.flowww;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;

public class AddActivity extends AppCompatActivity {
    public final String TAG = "AddActivity";
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://flowww.firebaseio.com/");

        setContentView(R.layout.activity_add);

    }

    public void uploadNewWaterSource(View v) {
        EditText nameInput = (EditText) findViewById(R.id.set_name);
        String name = nameInput.getText().toString();
        EditText locationDescriptionInput = (EditText) findViewById(R.id.set_location_description);
        String locationDescription = locationDescriptionInput.getText().toString();
        RatingBar starsInput = (RatingBar) findViewById(R.id.set_rating);
        int stars = starsInput.getNumStars();
        boolean status = ((ToggleButton) findViewById(R.id.set_toggle)).isChecked();
        String review = ((EditText) findViewById(R.id.set_init_review)).getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("locationDescription", locationDescription);
        bundle.putInt("stars", stars);
        bundle.putBoolean("status", status);
        bundle.putString("review", review);


        FountainLocation fl = new FountainLocation("butts", true);

        ref.push().setValue(fl);


        Intent intent = new Intent(AddActivity.this, DetailsView.class);
        //intent.putExtra("EXTRA_TEXT", bundle); // not sure what string to use here...
        startActivity(intent);

        // how to add that data to the firebase?
        // and how do we want to add the location of the fountain? by current location
        // or pick on map? how to implement picking on map if that's what we go with?
    }

    private static class FountainLocation {
        String name;
        boolean isWorking;

        public FountainLocation(String name, boolean isWorking) {
            this.name = name;
            this.isWorking = isWorking;
        }
    }


}



