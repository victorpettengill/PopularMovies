package victor.pettengill.popularmovies.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import victor.pettengill.popularmovies.data.DBHelper;
import victor.pettengill.popularmovies.data.PopularMoviesContract;

/**
 * Created by appimagetech on 25/07/17.
 */

public class FavoriteMoviesProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        dbHelper = new DBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor;

        switch (match) {

            case MOVIES:

                cursor = db.query(PopularMoviesContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            case MOVIE_WITH_ID:

                String id = uri.getPathSegments().get(1);

                String mSelection = PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID+"=?";
                String[] mSelectionArgs = new String[]{id};

                cursor = db.query(PopularMoviesContract.MovieEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Not implemented");

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {

            case MOVIES:
                long id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, values);

                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(PopularMoviesContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Not implemented");

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int affectedItens = 0;

        switch (match) {

            case MOVIES:

                affectedItens = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                break;

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return affectedItens;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
