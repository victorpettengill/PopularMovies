package victor.pettengill.popularmovies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import victor.pettengill.popularmovies.beans.Movie;
import victor.pettengill.popularmovies.beans.Review;
import victor.pettengill.popularmovies.beans.Trailer;
import victor.pettengill.popularmovies.constants.SortType;
import victor.pettengill.popularmovies.data.DBHelper;
import victor.pettengill.popularmovies.data.PopularMoviesContract;
import victor.pettengill.popularmovies.network.NetworkUtils;

/**
 * Created by victorfernandes on 05/03/17.
 */

public class MoviesDao {

    private final String BASE_API_URL = "http://api.themoviedb.org/3/";
    private final String API_KEY = "?api_key=1668c5dd4e446f81205b03c3e7584af1";

    private final String MOVIES_API = "movie/%s";
    private final String MOVIES_TRAILERS_API = "movie/%s/videos";
    private final String MOVIES_REVIEWS_API = "movie/%s/reviews";

    private static MoviesDao instance;
    private DBHelper dbHelper;

    public static MoviesDao getInstance(Context context) {

        if(instance == null) {
            instance = new MoviesDao(context);
        }

        return instance;
    }

    public MoviesDao(Context context) {

        dbHelper = new DBHelper(context);

    }

    public ArrayList<Movie> getMovies(String sortType) {

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
            movies = getFavoriteMovies();
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

    public ArrayList<Movie> getFavoriteMovies() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(PopularMoviesContract.MovieEntry.TABLE_NAME, null, null, null, null, null, PopularMoviesContract.MovieEntry.COLUMN_MOVIEW_RELEASE_DATE);

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

    public void addMovieAsFavorite(Movie movie) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER, movie.getMoviePoster());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_THUMBNAIL, movie.getMoviePosterThumbnail());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_SYSNOPSIS, movie.getSynopsis());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_USER_RATING, movie.getUserRating());
        cv.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIEW_RELEASE_DATE, movie.getReleaseDate().getTime());

        db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, cv);

    }

    public void removeFromFavorite(Movie movie) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID+"=?", new String[]{movie.getMovieId()});

    }

    public boolean movieIsFavorite(Movie movie) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(PopularMoviesContract.MovieEntry.TABLE_NAME, null, PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID+"=?", new String[]{movie.getMovieId()}, null, null, PopularMoviesContract.MovieEntry.COLUMN_MOVIEW_RELEASE_DATE);

        boolean isFavorite = false;

        if(cursor.moveToFirst()) {
            isFavorite = true;
        }

        cursor.close();

        return isFavorite;
    }

}