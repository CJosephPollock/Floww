package edu.uw.jpollock.flowww;

/**
 * Created by iguest on 5/30/16.
 */

// REVIEW INFORMATION
public class Review {
    float rating; // review rating
    String desc; // review description
    Long timestamp; // time of review add
    String title; // title/subject of review

    public Review() {}


    public Review(float rating, String desc, String title, Long timestamp) {
        this.rating = rating;
        this.desc = desc;
        this.timestamp = timestamp;
        this.title = title;
    }
}