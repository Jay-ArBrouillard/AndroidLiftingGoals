package com.example.liftinggoals;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
                    + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_ID + TEXT_TYPE
                    + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE
                    + COMMA_SEP +
                    UserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + " )";

    private static final String DELETE_TABLE_USERS = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LiftingGoals.db";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //inner class that defines the schema
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_USERS);
        onCreate(db);
    }
}
