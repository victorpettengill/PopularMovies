package victor.pettengill.popularmovies.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by victorfernandes on 04/03/17.
 */

public class Movie implements Parcelable{

    private String movieId;
    private String originalTitle;
    private String moviePoster;
    private String moviePosterThumbnail;
    private String synopsis;
    private double userRating;
    private Date releaseDate;
    private List<Trailer> trailers;
    private List<Review> reviews;

    public Movie() {
    }

    public Movie(JSONObject object) throws JSONException, ParseException {

        if(object.has("id")) {
            movieId = object.getString("id");
        }

        if(object.has("original_title")) {
            originalTitle = object.getString("original_title");
        }

        if(object.has("poster_path")) {
            String poster = object.getString("poster_path");

            moviePoster = "https://image.tmdb.org/t/p/original"+poster;
            moviePosterThumbnail = "https://image.tmdb.org/t/p/w500"+poster;

        }

        if(object.has("overview")) {
            synopsis = object.getString("overview");
        }

        if(object.has("vote_average")) {
            userRating = object.getDouble("vote_average");
        }

        if(object.has("release_date")) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            releaseDate = sdf.parse(object.getString("release_date"));

        }

    }

    protected Movie(Parcel in) {
        movieId = in.readString();
        originalTitle = in.readString();
        moviePoster = in.readString();
        moviePosterThumbnail = in.readString();
        synopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = new Date(in.readLong());
        in.readList(trailers, Trailer.class.getClassLoader());
        in.readList(reviews, Review.class.getClassLoader());

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePosterThumbnail() {
        return moviePosterThumbnail;
    }

    public void setMoviePosterThumbnail(String moviePosterThumbnail) {
        this.moviePosterThumbnail = moviePosterThumbnail;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(originalTitle);
        dest.writeString(moviePoster);
        dest.writeString(moviePosterThumbnail);
        dest.writeString(synopsis);
        dest.writeDouble(userRating);
        dest.writeLong(releaseDate.getTime());
        dest.writeList(trailers);
        dest.writeList(reviews);

    }
}