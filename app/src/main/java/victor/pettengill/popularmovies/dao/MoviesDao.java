package victor.pettengill.popularmovies.dao;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import victor.pettengill.popularmovies.beans.Movie;
import victor.pettengill.popularmovies.constants.SortType;
import victor.pettengill.popularmovies.network.NetworkUtils;

/**
 * Created by victorfernandes on 05/03/17.
 */

public class MoviesDao {

    private final String APIURL = "http://api.themoviedb.org/3/movie/%s?api_key=1668c5dd4e446f81205b03c3e7584af1";
    private static MoviesDao instance;

    public static MoviesDao getInstance() {

        if(instance == null) {
            instance = new MoviesDao();
        }

        return instance;
    }

    public ArrayList<Movie> getMovies(String sortType) {

        String url = String.format(APIURL, sortType);

        ArrayList<Movie> movies = null;

        try {

            URL url1 = new URL(url);

            String string = NetworkUtils.getResponseFromUrl(url1);

            JSONObject response = new JSONObject(string);

            JSONArray jsonArray = response.getJSONArray("results");

            if(jsonArray.length() > 0) {

                movies = new ArrayList<Movie>();

                for(int i=0; i < jsonArray.length(); i++) {

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

        return movies;

    }

//    public void getMovies(String sortType, final MoviesRequestResult requestResult) {
//
//        String url = String.format(APIURL, sortType);
//
//        new AsyncTask<String, Void, List<Movie>>(){
//
//            @Override
//            protected List<Movie> doInBackground(String... params) {
//
//                List<Movie> movies = null;
//
//                try {
//
//                    URL url1 = new URL(params[0]);
//
//                    String string = NetworkUtils.getResponseFromUrl(url1);
//
//                    JSONObject response = new JSONObject(string);
//
//                    JSONArray jsonArray = response.getJSONArray("results");
//
//                    if(jsonArray.length() > 0) {
//
//                        movies = new ArrayList<Movie>();
//
//                        for(int i=0; i < jsonArray.length(); i++) {
//
//                            Movie movie = new Movie(jsonArray.getJSONObject(i));
//                            movies.add(movie);
//
//                        }
//
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                return movies;
//            }
//
//            @Override
//            protected void onPostExecute(List<Movie> movies) {
//                super.onPostExecute(movies);
//
//                if(movies != null) {
//
//                    requestResult.onSuccess(movies);
//
//                } else {
//                    requestResult.onError("Error fetching movies, verify your connection and try again later.");
//                }
//
//            }
//
//        }.execute(url);
//
//    }

    public interface MoviesRequestResult{

        void onError(String error);
        void onSuccess(List<Movie> movies);

    }

}