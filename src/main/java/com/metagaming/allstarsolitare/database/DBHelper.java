package com.metagaming.allstarsolitare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.metagaming.allstarsolitare.database.DBContract.DBEntry;
import com.metagaming.allstarsolitare.database.DBContract.AccountsEntry;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "all_star_solitaire.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE SCORES TABLE
        final String CREATE_SCORES_QUERY = "CREATE TABLE "+
                DBEntry.TABLE_NAME+" ("+
                DBEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DBEntry.COLUMN_USER+" TEXT NOT NULL, "+
                DBEntry.COLUMN_POINTS+" INTEGER NOT NULL, "+
                DBEntry.COLUMN_TIME+" TEXT NOT NULL);";
        //
        sqLiteDatabase.execSQL(CREATE_SCORES_QUERY);

        //CREATE ACCOUNTS TABLE
        final String CREATE_ACCOUNTS_QUERY = "CREATE TABLE "+
                AccountsEntry.TABLE_NAME+" ("+
                AccountsEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                AccountsEntry.COLUMN_USER_NAME+" TEXT NOT NULL, "+
                AccountsEntry.COLUMN_AUTOLOG +" TEXT NOT NULL, "+
                AccountsEntry.COLUMN_CURRENCY+" INTEGER NOT NULL, "+
                AccountsEntry.COLUMN_EXP+" INTEGER NOT NULL," +
                AccountsEntry.COLUMN_LEVEL+" INTEGER NOT NULL);";
        //
        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_QUERY);

        //INSERT GUEST ACCOUNT
        final String INSERT_GUEST_QUERY = "INSERT INTO "+AccountsEntry.TABLE_NAME+
                " ("+AccountsEntry.COLUMN_USER_NAME+", "+AccountsEntry.COLUMN_AUTOLOG +
                ", "+AccountsEntry.COLUMN_CURRENCY+", "+AccountsEntry.COLUMN_LEVEL+", "+
                AccountsEntry.COLUMN_EXP+") VALUES (\"guestPlayer\", \"false\", 500, 1, 0);";

        sqLiteDatabase.execSQL(INSERT_GUEST_QUERY);

        //INSERT TEST SCORE INTO SCORES
        final String INSERT_TEST_SCORE_QUERY1 = "INSERT INTO "+DBEntry.TABLE_NAME+
                " ("+DBEntry.COLUMN_USER+", "+DBEntry.COLUMN_POINTS +
                ", "+DBEntry.COLUMN_TIME+") VALUES (\"Testing Player1\", 16954, \"12:35\");";
        final String INSERT_TEST_SCORE_QUERY2 = "INSERT INTO "+DBEntry.TABLE_NAME+
                " ("+DBEntry.COLUMN_USER+", "+DBEntry.COLUMN_POINTS +
                ", "+DBEntry.COLUMN_TIME+") VALUES (\"Testing Player2\", 13337, \"13:37\");";
        final String INSERT_TEST_SCORE_QUERY3 = "INSERT INTO "+DBEntry.TABLE_NAME+
                " ("+DBEntry.COLUMN_USER+", "+DBEntry.COLUMN_POINTS +
                ", "+DBEntry.COLUMN_TIME+") VALUES (\"Imma Noob\", 147, \"1:12\");";

        sqLiteDatabase.execSQL(INSERT_TEST_SCORE_QUERY1);
        sqLiteDatabase.execSQL(INSERT_TEST_SCORE_QUERY2);
        sqLiteDatabase.execSQL(INSERT_TEST_SCORE_QUERY3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDb, int i, int i1) {

    }
}
