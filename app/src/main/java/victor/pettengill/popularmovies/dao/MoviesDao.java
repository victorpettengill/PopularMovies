package victor.pettengill.popularmovies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import victor.pettengill.popularmovies.beans.Movie;
import victor.pettengill.popularmovies.beans.Review;
import victor.pettengill.popularmovies.beans.Trailer;
import victor.pettengill.popularmovies.constants.SortType;
import victor.pettengill.popularmovies.data.PopularMoviesContract;
import victor.pettengill.popularmovies.network.NetworkUtils;

/**
 * Created by victorfernandes on 05/03/17.
 */

public class MoviesDao {

    private final String BASE_API_URL = "http://api.themoviedb.org/3/";
    private final String API_KEY = "?api_key=";

    private final String MOVIES_API = "movie/%s";
    private final String MOVIES_TRAILERS_API = "movie/%s/videos";
    private final String MOVIES_REVIEWS_API = "movie/%s/reviews";

    private static MoviesDao instance;

    public static MoviesDao getInstance() {

        if(instance == null) {
            instance = new MoviesDao();
        }

        return instance;
    }

    public ArrayList<Movie> getMovies(Context context, String sortType) {

        ArrayList<Movie> movies = null;

        if(!sortType.equals(SortType.FAVORITES)) {

            String url = String.format(BASE_API_URL + MOVIES_API + API_KEY, sortType);

            try {

                URL url1 = new URL(url);

                String string = NetworkUtils.getResponseFromUrl(url1);

                JSONObject response = new JSONObject(string);

                JSONArray jsonArray = response.getJSONArray("results");

                if (jsonArray.length() > 0) {

                    movies = new ArrayList<Movie>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Movie movie = new Movie(jsonArray.getJSONObject(i));
                        movies.add(movie);

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            movies = getFavoriteMovies(context);
        }

        return movies;

    }

    public ArrayList<Trailer> getMovieTrailers(Movie movie) {

        String url = String.format(BASE_API_URL+ MOVIES_TRAILERS_API + API_KEY, movie.getMovieId());

        ArrayList<Trailer> trailers = null;

        try {

            URL url1 = new URL(url);

            String string = NetworkUtils.getResponseFromUrl(url1);

            JSONObject response = new JSONObject(string);

            JSONArray jsonArray = response.getJSONArray("results");

            if(jsonArray.length() > 0) {

                trailers = new ArrayList<Trailer>();

                for(int i=0; i < jsonArray.length(); i++) {

                    Trailer trailer = new Trailer(jsonArray.getJSONObject(i));
                    trailers.add(trailer);

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return trailers;
    }

    public ArrayList<Review> getMoviewReviews(Movie movie) {

        String url = String.format(BASE_API_URL+ MOVIES_REVIEWS_API + API_KEY, movie.getMovieId());

        ArrayList<Review> reviews = null;

        try {

            URL url1 = new URL(url);

            String string = NetworkUtils.getResponseFromUrl(url1);

            JSONObject response = new JSONObject(string);

            JSONArray jsonArray = response.getJSONArray("results");

            if(jsonArray.length() > 0) {

                reviews = new ArrayList<Review>();

                for(int i=0; i < jsonArray.length(); i++) {

                    Review review = new Review(jsonArray.getJSONObject(i));
                    reviews.add(review);

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public ArrayList<Movie> getFavoriteMovies(Context context) {

        Cursor cursor = context.getContentResolver().query(PopularMoviesContract.MovieEntry.CONTENT_URI, null, null, null, PopularMoviesContract.MovieEntry.COLUMN_MOVIEW_RELEASE_DATE);

        ArrayList<Movie> movies = null;

        while(cursor.moveToNext()) {

            if(movies == null) {
                movies = new ArrayList<>();
            }

            Movie movie = new Movie();
            movie.setMovieId(cursor.getString(1));
            movie.setOriginalTitle(cursor.getString(2));
            movie.setMoviePoster(cursor.getString(3));
            movie.setMoviePosterThumbnail(cursor.getString(4));
            movie.setSynopsis(cursor.getString(5));
            movie.setUserRating(cursor.getDouble(6));
            movie.setReleaseDate(new Date(cursor.getLong(7)));

            movies.add(movie);
        }

        cursor.close();

        return movies;
    }

    public void addMovieAsFavorite(Context context, Movie movie) {

        ContentValues cv = new ContentValues();
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER, movie.getMoviePoster());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_THUMBNAIL, movie.getMoviePosterThumbnail());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_SYSNOPSIS, movie.getSynopsis());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_USER_RATING, movie.getUserRating());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIEW_RELEASE_DATE, movie.getReleaseDate().getTime());

        Uri uri = context.getContentResolver().insert(PopularMoviesContract.MovieEntry.CONTENT_URI, cv);

        if(uri != null) {
            Log.i("uri", uri.toString());
        }

    }

    public void removeFromFavorite(Context context, Movie movie) {

        context.getContentResolver().delete(PopularMoviesContract.MovieEntry.CONTENT_URI, PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID+"=?", new String[]{movie.getMovieId()});

    }

    public boolean movieIsFavorite(Context context, Movie movie) {

        Uri uri = PopularMoviesContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getMovieId()).build();

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        boolean isFavorite = false;

        if(cursor != null) {

            if (cursor.moveToFirst()) {
                isFavorite = true;
            }

            cursor.close();

        }

        return isFavorite;
    }

}