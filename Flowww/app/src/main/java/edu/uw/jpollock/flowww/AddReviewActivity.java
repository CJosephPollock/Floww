package edu.uw.jpollock.flowww;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.client.Firebase;

/**
 * Created by iguest on 5/30/16.
 */
public class AddReviewActivity extends AppCompatActivity{
    private Firebase ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        Firebase.setAndroidContext(this);

        // FIREBASE OF KEY REVIEWS
        final String key = getIntent().getExtras().getString("key");
        ref = new Firebase("https://flowww.firebaseio.com/" + key + "/reviews");


        // ON CLICK ADD REVIEW - CREATES REVIEW WITH RATING, SUBJECT/REVIEW TITLE, AND REVIEW DESCRIPTION, PUSHED INFO
        // TO FIREBASE, THEN TAKES YOU BACK TO DETAILS VIEW OF WATER SOURCE
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar starsInput = (RatingBar) findViewById(R.id.set_review_rating);
                final float stars = starsInput.getRating();
                final String reviewDesc = ((EditText) findViewById(R.id.review_text)).getText().toString();

                final String reviewTitle = ((EditText) findViewById(R.id.review_title)).getText().toString();

                Review newReview = new Review(stars, reviewDesc, reviewTitle, System.currentTimeMillis()/1000);
                ref.push().setValue(newReview);


                Intent intent = new Intent(AddReviewActivity.this, DetailsView.class);
                Bundle b = new Bundle();
                b.putString("lastKey", key);
                intent.putExtras(b);
                Toast.makeText(getApplicationContext(), "Review Submitted", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }


}
