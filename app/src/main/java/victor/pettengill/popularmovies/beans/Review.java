package victor.pettengill.popularmovies.beans;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by appimagetech on 24/07/17.
 */

public class Review {

    private String reviewId;
    private String reviewAuthor;
    private String reviewContent;

    public Review() {
    }

    public Review(JSONObject object) throws JSONException, ParseException {

        if(object.has("id")) {
            reviewId = object.getString("id");
        }

        if(object.has("author")) {
            reviewAuthor = object.getString("author");
        }

        if(object.has("content")) {
            reviewContent = object.getString("content");
        }

    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}