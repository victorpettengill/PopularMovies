package victor.pettengill.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import victor.pettengill.popularmovies.adapter.ReviewAdapter;
import victor.pettengill.popularmovies.adapter.TrailerAdapter;
import victor.pettengill.popularmovies.beans.Movie;
import victor.pettengill.popularmovies.beans.Review;
import victor.pettengill.popularmovies.beans.Trailer;
import victor.pettengill.popularmovies.dao.MoviesDao;
import victor.pettengill.popularmovies.network.NetworkUtils;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, TrailerAdapter.TrailerClickListener {

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.releasedate) TextView releaseDate;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.synopsis) TextView synopsis;

    @BindView(R.id.holderInfo) LinearLayout holderinfo;
    @BindView(R.id.progressInfo) ProgressBar progressInfo;
    @BindView(R.id.trailersRecycler) RecyclerView trailersRecycler;
    @BindView(R.id.reviewsRecycler) RecyclerView reviewsRecycler;

    @BindView(R.id.trailerShare) Button trailerShare;

    private Movie movie;

    private static final int MOVIE_LOADER = 22;
    private boolean favorite = false;

    private static final String ARGS_EXTRA = "movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        movie = getIntent().getParcelableExtra("movie");

        if(savedInstanceState != null) {

            if(savedInstanceState.containsKey(ARGS_EXTRA)) {

                movie = savedInstanceState.getParcelable(ARGS_EXTRA);

                progressInfo.setVisibility(View.GONE);
                holderinfo.setVisibility(View.VISIBLE);

                if(movie.getTrailers() != null) {

                    trailerShare.setVisibility(View.VISIBLE);

                    trailersRecycler.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                    trailersRecycler.setAdapter(new TrailerAdapter(MovieDetailsActivity.this, movie.getTrailers(), this));

                }

                if(movie.getReviews() != null) {

                    reviewsRecycler.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                    reviewsRecycler.setAdapter(new ReviewAdapter(MovieDetailsActivity.this, movie.getReviews()));

                }

                updateUI();

                verifyFavorite();


            }

        } else {

            if(NetworkUtils.isNetworkAvailable(MovieDetailsActivity.this)) {
                getMovieTrailersAndReviews();
            }

            updateUI();

            verifyFavorite();


        }

    }

    private void updateUI() {

        Picasso.with(this).load(movie.getMoviePosterThumbnail()).into(image);
        title.setText(movie.getOriginalTitle());

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        releaseDate.setText(String.format(getString(R.string.releasedate), format.format(movie.getReleaseDate())));
        rating.setText(String.format(getString(R.string.rating), movie.getUserRating()));
        synopsis.setText(movie.getSynopsis());

    }

    @OnClick(R.id.trailerShare) void onShareClicked() {

        if(movie != null && movie.getTrailers() != null && movie.getTrailers().size() > 0) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("http://www.youtube.com/watch?v=%s", movie.getTrailers().get(0).getTrailerKey()));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,  getString(R.string.share_with)));

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(movie != null) {
            outState.putParcelable(ARGS_EXTRA, movie);
        }

    }

    public void verifyFavorite() {

        new AsyncTask<Movie, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Movie... params) {
                return MoviesDao.getInstance().movieIsFavorite(MovieDetailsActivity.this, params[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                favorite = aBoolean;
                invalidateOptionsMenu();

            }

        }.execute(movie);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.favorite) {

            if (!favorite) {

                new AsyncTask<Movie, Void, Void>() {

                    @Override
                    protected Void doInBackground(Movie... params) {
                        MoviesDao.getInstance().addMovieAsFavorite(MovieDetailsActivity.this, params[0]);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        favorite = true;
                        invalidateOptionsMenu();

                    }

                }.execute(movie);

            } else {

                new AsyncTask<Movie, Void, Void>() {

                    @Override
                    protected Void doInBackground(Movie... params) {
                        MoviesDao.getInstance().removeFromFavorite(MovieDetailsActivity.this, params[0]);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        favorite = false;
                        invalidateOptionsMenu();

                    }

                }.execute(movie);

            }

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(!favorite) {
            getMenuInflater().inflate(R.menu.menu_movie_off, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_movie_on, menu);
        }

        return true;
    }

    private void getMovieTrailersAndReviews() {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(MOVIE_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER, null, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER, null, this);
        }

    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                progressInfo.setVisibility(View.VISIBLE);

                onForceLoad();
            }

            @Override
            public Movie loadInBackground() {

                List<Trailer> trailers = MoviesDao.getInstance().getMovieTrailers(movie);
                List<Review> reviews = MoviesDao.getInstance().getMoviewReviews(movie);

                movie.setTrailers(trailers);
                movie.setReviews(reviews);

                return movie;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

        progressInfo.setVisibility(View.GONE);
        holderinfo.setVisibility(View.VISIBLE);

        if(movie.getTrailers() != null) {

            trailerShare.setVisibility(View.VISIBLE);

            trailersRecycler.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            trailersRecycler.setAdapter(new TrailerAdapter(MovieDetailsActivity.this, data.getTrailers(), this));

        }

        if(movie.getReviews() != null) {

            reviewsRecycler.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            reviewsRecycler.setAdapter(new ReviewAdapter(MovieDetailsActivity.this, data.getReviews()));

        }

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }

    @Override
    public void onClick(Trailer trailer) {

        openTrailer(trailer.getTrailerKey());

    }

    public void openTrailer(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));

        //Try to open on youtube app, if not available, use web browser
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }

    }

}
