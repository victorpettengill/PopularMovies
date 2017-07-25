package victor.pettengill.popularmovies.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by appimagetech on 24/07/17.
 */

public class Review implements Parcelable{

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

    protected Review(Parcel in) {
        reviewId = in.readString();
        reviewAuthor = in.readString();
        reviewContent = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(reviewAuthor);
        dest.writeString(reviewContent);
    }
}