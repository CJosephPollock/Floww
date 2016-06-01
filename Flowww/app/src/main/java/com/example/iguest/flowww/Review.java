package com.example.iguest.flowww;

/**
 * Created by iguest on 5/30/16.
 */
public class Review {
    float rating;
    String desc;
    Long timestamp;

    public Review() {}


    public Review(float rating, String desc, Long timestamp) {
        this.rating = rating;
        this.desc = desc;
        this.timestamp = timestamp;
    }
}