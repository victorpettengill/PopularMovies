package victor.pettengill.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import victor.pettengill.popularmovies.beans.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.releasedate) TextView releaseDate;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.synopsis) TextView synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Movie movie = getIntent().getParcelableExtra("movie");

        Picasso.with(this).load(movie.getMoviePosterThumbnail()).into(image);
        title.setText(movie.getOriginalTitle());

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        releaseDate.setText(String.format(getString(R.string.releasedate), format.format(movie.getReleaseDate())));

        rating.setText(String.format(getString(R.string.rating), movie.getUserRating()));
        synopsis.setText(movie.getSynopsis());

    }

}
