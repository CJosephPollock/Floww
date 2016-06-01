package edu.uw.jpollock.flowww;

import java.util.ArrayList;

/**
 * Created by iguest on 5/30/16.
 */
public class FountainLocation {
    String name;
    String locationDescription;
    boolean isWorking;
    ArrayList<Review> reviews;
    double lat;
    double lng;


    public FountainLocation(String name, String locationDescription, boolean isWorking, Review initialReview, double lat, double lng) {
        this.name = name;
        this.isWorking = isWorking;
        this.locationDescription = locationDescription;
        reviews = new ArrayList<Review>();
        reviews.add(initialReview);
        this.lat = lat;
        this.lng = lng;
    }
}