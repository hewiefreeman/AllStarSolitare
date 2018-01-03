package com.metagaming.allstarsolitare.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.metagaming.allstarsolitare.database.DBContract;
import com.metagaming.allstarsolitare.database.DBHelper;
import com.metagaming.allstarsolitare.database.DBContract.DBEntry;


public class AllStarContentProvider extends ContentProvider {

    //SCORES MATCHER
    private static final int SCORES = 100;
    private static final int SCORE_FROM_ID = 101;
    //
    private static final int USERS = 200;
    private static final int USER_FROM_NAME = 201;

    //
    public static final String ACTION_DATA_UPDATED = "com.metagaming.allstarsolitare.ACTION_DATA_UPDATED";

    //
    private UriMatcher uriMatcher = buildUriMatcher();

    //
    private DBHelper dbHelper;

    //UriMatcher BUILDER
    private static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DBContract.AUTHORITY, DBContract.PATH_SCORES, SCORES);
        matcher.addURI(DBContract.AUTHORITY, DBContract.PATH_SCORES_WITH_SYMBOL, SCORE_FROM_ID);
        //
        matcher.addURI(DBContract.AUTHORITY, DBContract.PATH_USERS, USERS);
        matcher.addURI(DBContract.AUTHORITY, DBContract.PATH_USERS_WITH_NAME, USER_FROM_NAME);

        //
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if(uriMatcher.match(uri) == SCORES){
            cursor = db.query(DBEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    DBEntry.COLUMN_POINTS+" DESC");
        }else if(uriMatcher.match(uri) == SCORE_FROM_ID){
            cursor = db.query(DBEntry.TABLE_NAME,
                    projection,
                    DBEntry._ID + " = ?",
                    new String[]{DBEntry.getIdFromUri(uri)},
                    null,
                    null,
                    sortOrder);
        }else if(uriMatcher.match(uri) == USERS){
            cursor = db.query(DBContract.AccountsEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
        }else if(uriMatcher.match(uri) == USER_FROM_NAME){
            cursor = db.query(DBContract.AccountsEntry.TABLE_NAME,
                    projection,
                    DBContract.AccountsEntry.COLUMN_USER_NAME + " = ?",
                    new String[]{DBContract.AccountsEntry.getNameFromUri(uri)},
                    null,
                    null,
                    sortOrder);
        }else{
            throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        //
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Unknown URI:" + uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(uriMatcher.match(uri) == SCORES){
            db.insert(DBEntry.TABLE_NAME,
                    null,
                    contentValues);

            returnUri = DBEntry.URI;
        }else{
            throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        //
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Unknown URI:" + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Unknown URI:" + uri);
    }
}
