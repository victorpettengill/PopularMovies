package victor.pettengill.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by appimagetech on 24/07/17.
 */

public class PopularMoviesContract {

    public static final String AUTHORITY = "victor.pettengill.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public PopularMoviesContract() {
    }

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_MOVIE_POSTER_THUMBNAIL = "moviePosterThumbnail";
        public static final String COLUMN_MOVIE_SYSNOPSIS = "synopsis";
        public static final String COLUMN_MOVIE_USER_RATING = "userRating";
        public static final String COLUMN_MOVIEW_RELEASE_DATE = "releaseDate";

    }

    public static final class TrailerEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_TRAILER_ID = "trailerId";
        public static final String COLUMN_TRAILER_NAME = "trailerName";
        public static final String COLUMN_TRAILER_SITE = "trailerSite";
        public static final String COLUMN_TRAILER_TYPE = "trailerType";
        public static final String COLUMN_TRAILER_KEY = "trailerKey";
        public static final String COLUMN_MOVIE_ID = "movieId";

    }

    public static final class ReviewEntry implements BaseColumns {

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_ID = "reviewId";
        public static final String COLUMN_REVIEW_AUTHOR = "reviewAuthor";
        public static final String COLUMN_REVIEW_CONTENT = "reviewContent";
        public static final String COLUMN_MOVIE_ID = "movieId";

    }

}