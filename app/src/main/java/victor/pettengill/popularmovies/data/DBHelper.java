package victor.pettengill.popularmovies.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by appimagetech on 24/07/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE_MOVIES = "CREATE TABLE " +
                PopularMoviesContract.MovieEntry.TABLE_NAME + " ( " +
                PopularMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_THUMBNAIL + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.COLUMN_MOVIE_SYSNOPSIS + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.COLUMN_MOVIE_USER_RATING + " DOUBLE NOT NULL, " +
                PopularMoviesContract.MovieEntry.COLUMN_MOVIEW_RELEASE_DATE + " TIMESTAMP NOT NULL" +
                ");";

        final String SQL_CREATE_TABLE_TRAILERS = "CREATE TABLE " +
                PopularMoviesContract.TrailerEntry.TABLE_NAME + " ( " +
                PopularMoviesContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL, " +
                PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE + " TEXT NOT NULL, " +
                PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_TYPE + " TEXT NOT NULL, " +
                PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
                PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_TABLE_REVIEWS = "CREATE TABLE " +
                PopularMoviesContract.ReviewEntry.TABLE_NAME + " ( " +
                PopularMoviesContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_TABLE_MOVIES);
        db.execSQL(SQL_CREATE_TABLE_TRAILERS);
        db.execSQL(SQL_CREATE_TABLE_REVIEWS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ PopularMoviesContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ PopularMoviesContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ PopularMoviesContract.ReviewEntry.TABLE_NAME);

        onCreate(db);

    }
}
