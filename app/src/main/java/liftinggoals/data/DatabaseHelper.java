package liftinggoals.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


import liftinggoals.classes.User;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "liftingGoals.db";
    public static final int DATABASE_VERSION = 8;

    public SQLiteDatabase myDB;


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(RoutineTable.SQL_CREATE_ROUTINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(RoutineTable.SQL_DROP_ROUTINE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void openDB()
    {
        myDB = getWritableDatabase();
    }

    public void closeDB()
    {
        if (myDB != null && myDB.isOpen())
        {
            myDB.close();
        }
    }

    public long insert(String name, String description)
    {
        return RoutineTable.insert(myDB, name, description);
    }

}
