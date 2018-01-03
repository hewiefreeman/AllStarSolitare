package com.metagaming.allstarsolitare.database;

import android.net.Uri;
import android.provider.BaseColumns;


public class DBContract {

    public static final String AUTHORITY = "com.metagaming.allstarsolitare";
    public static final String PATH_SCORES = "scores";
    public static final String PATH_SCORES_WITH_SYMBOL = "scores/*";
    public static final String PATH_USERS = "accounts";
    public static final String PATH_USERS_WITH_NAME = "accounts/*";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final class DBEntry implements BaseColumns{
        public static final String TABLE_NAME = "scores";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_POINTS = "points";
        static final String COLUMN_TIME = "time";

        //
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_SCORES).build();
        public static String getIdFromUri(Uri scoreUri){
            return scoreUri.getLastPathSegment();
        }
    }

    public static final class AccountsEntry implements BaseColumns{
        public static final String TABLE_NAME = "accounts";
        public static final String COLUMN_USER_NAME = "userName";
        public static final String COLUMN_AUTOLOG = "autolog";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_EXP = "exp";

        //
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_USERS).build();
        public static String getNameFromUri(Uri accountUri){
            return accountUri.getLastPathSegment();
        }
    }
}