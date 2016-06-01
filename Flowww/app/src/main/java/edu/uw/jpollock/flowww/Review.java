package edu.uw.jpollock.flowww;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by iguest on 5/30/16.
 */

// REVIEW INFORMATION
@JsonSerialize
public class Review {
    float rating; // review rating
    String desc; // review description
    String timestamp; // time of review add
    String title; // title/subject of review

    public Review() {}


    public Review(float rating, String desc, String title, String timestamp) {
        this.rating = rating;
        this.desc = desc;
        this.timestamp = timestamp;
        this.title = title;
    }
}