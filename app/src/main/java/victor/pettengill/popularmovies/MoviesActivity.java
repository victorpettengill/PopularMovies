package victor.pettengill.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import victor.pettengill.popularmovies.adapter.MovieAdapter;
import victor.pettengill.popularmovies.beans.Movie;
import victor.pettengill.popularmovies.dao.MoviesDao;
import victor.pettengill.popularmovies.constants.SortType;

public class MoviesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>, MovieAdapter.MovieClickListener {

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler) RecyclerView recyclerView;

    private String currentSort;

    private static final int MOVIE_LOADER = 22;
    private static final String LOADER_QUERY_KEY = "query";
    private static final String ARGS_EXTRA = "movies";

    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MoviesActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMovies(currentSort);

            }
        });

        if(savedInstanceState != null) {

            if(savedInstanceState.containsKey(ARGS_EXTRA)) {

                movies = savedInstanceState.getParcelableArrayList(ARGS_EXTRA);
                recyclerView.setAdapter(new MovieAdapter(MoviesActivity.this, movies, MoviesActivity.this));

            }

        } else {

            getMovies(SortType.POPULARITY);

        }

    }

    public void getMovies(String sortType) {

        currentSort = sortType;

        Bundle bundle = new Bundle();
        bundle.putString(LOADER_QUERY_KEY, sortType);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(MOVIE_LOADER);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER, bundle, this);
        }

//        MoviesDao.getInstance().getMovies(sortType, new MoviesDao.MoviesRequestResult() {
//            @Override
//            public void onError(String error) {
//
//                Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG).show();
//                refreshLayout.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onSuccess(final List<Movie> movies) {
//
//                refreshLayout.setRefreshing(false);
//                recyclerView.setAdapter(new MovieAdapter(MoviesActivity.this, movies, new MovieAdapter.MovieClickListener() {
//                    @Override
//                    public void onClick(int position) {
//
//                        Intent i = new Intent(MoviesActivity.this, MovieDetailsActivity.class);
//                        i.putExtra("movie", movies.get(position));
//                        startActivity(i);
//
//                    }
//                }));
//
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(movies != null) {

            outState.putParcelableArrayList(ARGS_EXTRA, movies);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.top_rated:
                getMovies(SortType.HIGHESTRATED);
                getSupportActionBar().setTitle(getString(R.string.toprated));
                break;

            case R.id.popular:
                getMovies(SortType.POPULARITY);
                getSupportActionBar().setTitle(getString(R.string.popular));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(true);
                    }
                });

                forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                return MoviesDao.getInstance().getMovies(args.getString(LOADER_QUERY_KEY));
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, final ArrayList<Movie> data) {

        if(data != null) {

            movies = data;

            refreshLayout.setRefreshing(false);
            recyclerView.setAdapter(new MovieAdapter(MoviesActivity.this, movies, MoviesActivity.this));

        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public void onClick(int position) {

        Intent i = new Intent(MoviesActivity.this, MovieDetailsActivity.class);
        i.putExtra("movie", movies.get(position));
        startActivity(i);

    }
}
