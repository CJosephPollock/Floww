package edu.uw.jpollock.flowww;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ReviewDetail extends AppCompatActivity {

    private ArrayList reviewsList;
    private ReviewAdapter adapter;
    private Firebase ref;
    ListView reviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        // sets listview of reviews added to water source
        reviews = (ListView)findViewById(R.id.listDetailsSourceReviews);

        Firebase.setAndroidContext(this);
        final String key = getIntent().getExtras().getString("lastKey");
        ref = new Firebase("https://flowww.firebaseio.com/" + key);

        // LIST OF REVIEWS
        reviewsList = new ArrayList<Review>();
        adapter = new ReviewAdapter(this, reviewsList);

        // LOADS LIST OF REVIEWS ADDED
        loadReviews();

        reviews.setAdapter(adapter);

    }

    // CUSTOM ADAPTER FOR REVIEWS
    public class ReviewAdapter extends ArrayAdapter<Review> {
        public ReviewAdapter(Context context, ArrayList<Review> reviews) {
            super(context, 0, reviews);
        }

        // VIEW OF REVIEW ITEMS/POSTS
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Get the data for the item in a given position
            Review review = getItem(position);
            //Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
            }
            //Lookup view for data population
            RatingBar rating = (RatingBar)convertView.findViewById(R.id.rating_item);
            TextView desc = (TextView)convertView.findViewById(R.id.desc_item);
            TextView reviewTitle = (TextView)convertView.findViewById(R.id.desc_title);

            //Set the fields for the "row"
            rating.setRating(review.rating);
            desc.setText(review.desc);
            reviewTitle.setText(review.title);



            return convertView;
        }
    }

    // LOADS REVIEWS LIST BY MOST RECENT
    public void loadReviews() {
        ref.child("reviews").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DataSnapshot> tempList = new ArrayList<DataSnapshot>();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    tempList.add(messageSnapshot);
                }

                Collections.reverse(tempList); // most recent first

                // GOES THROUGH DATA OF REVIEWS AND ADDS TO REVIEWSLIST TO SHOW IN LIST VIEW
                for(DataSnapshot messageSnapshot : tempList) {
                    //create a new review
                    float rating = Float.parseFloat(messageSnapshot.child("rating").getValue().toString());
                    String desc = messageSnapshot.child("desc").getValue().toString();
                    String title = messageSnapshot.child("title").getValue().toString();

                    Long time = Long.parseLong(messageSnapshot.child("timestamp").getValue().toString());

                    Review review = new Review(rating, desc, title, "29873592");

                    //add it to the arraylist of reviews
                    reviewsList.add(review);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

}

