package com.example.iguest.flowww;

import java.util.ArrayList;

/**
 * Created by iguest on 5/30/16.
 */

// WATER SOURCE/FOUNTAIN LOCATION INFORMATION
public class FountainLocation {
    String name; // name
    String locationDescription; // location description
    boolean isWorking; // working or not working
    ArrayList<Review> reviews; // list of reviews for water source
    double lat; // latitude
    double lng; // longitude


    public FountainLocation(String name, String locationDescription, boolean isWorking, Review initialReview, double lat, double lng) {
        this.name = name;
        this.isWorking = isWorking;
        this.locationDescription = locationDescription;
        reviews = new ArrayList<Review>();
        reviews.add(initialReview); // add initial review to list of reviews
        this.lat = lat;
        this.lng = lng;
    }
}