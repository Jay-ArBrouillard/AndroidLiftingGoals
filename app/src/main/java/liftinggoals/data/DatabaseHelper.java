package liftinggoals.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.List;

import liftinggoals.classes.User;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "liftingGoals.db";
    public static final int DATABASE_VERSION = 8;

    private SQLiteDatabase myDB;


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //inner class that defines the schema
    public static abstract class UserEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_TIMESTAMP = "time_stamp";
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
                UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserEntry.COLUMN_FIRST_NAME + " TEXT, " +
                UserEntry.COLUMN_LAST_NAME + " TEXT, " +
                UserEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        final String DELETE_TABLE_USERS = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
        db.execSQL(DELETE_TABLE_USERS);
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

    public long insert(String username, String password, String firstName, String lastName)
    {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USERNAME, username);
        values.put(UserEntry.COLUMN_PASSWORD, password);
        values.put(UserEntry.COLUMN_FIRST_NAME, firstName);
        values.put(UserEntry.COLUMN_LAST_NAME, lastName);

        return myDB.insert(UserEntry.TABLE_NAME, null, values);
    }

    public long update(int id, String username, String password, String firstName, String lastName)
    {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USERNAME, username);
        values.put(UserEntry.COLUMN_PASSWORD, password);
        values.put(UserEntry.COLUMN_FIRST_NAME, firstName);
        values.put(UserEntry.COLUMN_LAST_NAME, lastName);

        String where = UserEntry._ID + " = " + id;

        return myDB.update(UserEntry.TABLE_NAME, values, where, null);
    }

    public long delete(int id)
    {
        String where = UserEntry._ID + " = " + id;

        return myDB.delete(UserEntry.TABLE_NAME, where, null);
    }

    public String getUsername(String username)
    {
        String query = "SELECT username FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry.COLUMN_USERNAME  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {username});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            String name = null;

            while(c.moveToNext()){
                name = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USERNAME));
            }

            return name;
        }
    }

    public User getUser(String username, String password)
    {
        String query = "SELECT username,password FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry.COLUMN_USERNAME  + " = ? AND " + UserEntry.COLUMN_PASSWORD + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {username, password});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            User user = new User();

            while(c.moveToNext()){
                user.setUsername(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USERNAME)));
                user.setLast_name(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_PASSWORD)));
            }

            return user;
        }
    }

    public Cursor getAllUsers()
    {
        //String query = "SELECT * FROM " + UserEntry.TABLE_NAME;
        // return myDB.rawQuery(query, null);
        return myDB.query(UserEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    public long clear()
    {
        return myDB.delete(UserEntry.TABLE_NAME, null, null);
    }


}
