package liftinggoals.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import liftinggoals.classes.User;

public class RoutineTable{

    public static final String SQL_CREATE_ROUTINE_TABLE = "CREATE TABLE " +
            RoutineEntry.TABLE_NAME + " (" +
            RoutineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RoutineEntry.COLUMN_ROUTINE_ID + " TEXT NOT NULL, " +
            RoutineEntry.COLUMN_ROUTINE_NAME + " TEXT NOT NULL, " +
            RoutineEntry.COLUMN_DESCRIPTION + " TEXT " +
            ");";
    public static final String SQL_DROP_ROUTINE_TABLE = "DROP TABLE IF EXISTS " + RoutineEntry.TABLE_NAME;


    public static abstract class RoutineEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Routines";
        public static final String COLUMN_ROUTINE_ID = "routine_id";
        public static final String COLUMN_ROUTINE_NAME = "routine_name";
        public static final String COLUMN_DESCRIPTION = "description";
    }
    public static long insert(SQLiteDatabase myDB, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);

        return myDB.insert(RoutineEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int id, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);

        String where = RoutineEntry._ID + " = " + id;

        return myDB.update(RoutineEntry.TABLE_NAME, values, where, null);
    }

    public static long delete(SQLiteDatabase myDB, int id)
    {
        String where = RoutineEntry._ID + " = " + id;

        return myDB.delete(RoutineEntry.TABLE_NAME, where, null);
    }
/*
    public static String getUsername(String username)
    {
        String query = "SELECT username FROM " + RoutineEntry.TABLE_NAME + " WHERE " + RoutineEntry.COLUMN_USERNAME  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {username});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            String name = null;

            while(c.moveToNext()){
                name = c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_USERNAME));
            }

            return name;
        }
    }

    public User getRoutine(String username, String password)
    {
        String query = "SELECT username,password FROM " + RoutineEntry.TABLE_NAME + " WHERE " + RoutineEntry.COLUMN_USERNAME  + " = ? AND " + RoutineEntry.COLUMN_PASSWORD + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {username, password});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            User user = new User();

            while(c.moveToNext()){
                user.setUsername(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_USERNAME)));
                user.setLast_name(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_PASSWORD)));
            }

            return user;
        }
    }

    public Cursor getAllUsers()
    {
        //String query = "SELECT * FROM " + RoutineEntry.TABLE_NAME;
        // return myDB.rawQuery(query, null);
        return myDB.query(RoutineEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    public long clear()
    {
        return myDB.delete(RoutineEntry.TABLE_NAME, null, null);
    }

*/


}
