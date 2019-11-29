package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.models.UserModel;

public class UserTable {
    public static final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
            UserEntry.TABLE_NAME + " (" +
            UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
            UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
            UserEntry.COLUMN_IS_ADMIN + " INTEGER DEFAULT 0, " +
            UserEntry.COLUMN_LAST_LOGIN + " TEXT" +
            ");";
    public static final String SQL_DROP_USER_TABLE = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


    public static abstract class UserEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_IS_ADMIN = "admin";
        public static final String COLUMN_LAST_LOGIN = "last_login";
    }

    public static long insert(SQLiteDatabase myDB, String username, String password, int isAdmin, String lastLogin)
    {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USERNAME, username);
        values.put(UserEntry.COLUMN_PASSWORD, password);
        values.put(UserEntry.COLUMN_IS_ADMIN, isAdmin);
        values.put(UserEntry.COLUMN_LAST_LOGIN, lastLogin);

        return myDB.insert(UserEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int userId, String username, String password, int isAdmin, String lastLogin)
    {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USERNAME, username);
        values.put(UserEntry.COLUMN_PASSWORD, password);
        values.put(UserEntry.COLUMN_IS_ADMIN, isAdmin);
        values.put(UserEntry.COLUMN_LAST_LOGIN, lastLogin);

        return myDB.update(UserEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(userId)});
    }

    public static long delete(SQLiteDatabase myDB, int userId)
    {
        String where = UserEntry._ID + " = " + userId;

        return myDB.delete(UserEntry.TABLE_NAME, where, null);
    }

    public static long update(SQLiteDatabase myDB, String userName, String loginTime)
    {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_LAST_LOGIN, loginTime);

        return myDB.update(UserEntry.TABLE_NAME, values, "username = ?", new String[] {userName});
    }

    public static UserModel get(SQLiteDatabase myDB, int userId)
    {
        String query = "SELECT * FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(userId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            UserModel user = new UserModel();

            while(c.moveToNext()){
                user.setUserId(c.getInt(c.getColumnIndexOrThrow(UserEntry._ID)));
                user.setAdmin(c.getInt(c.getColumnIndexOrThrow(UserEntry.COLUMN_IS_ADMIN)));
                user.setUsername(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USERNAME)));
                user.setLastLogin(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_LAST_LOGIN)));
                user.setPassword(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_PASSWORD)));
            }
            return user;
        }
    }

    public static UserModel get(SQLiteDatabase myDB,  String username, String password)
    {
        String query = "SELECT * FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry.COLUMN_USERNAME  + " = ? AND " + UserEntry.COLUMN_PASSWORD + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {username, password});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            UserModel user = new UserModel();

            while(c.moveToNext()){
                user.setUserId(c.getInt(c.getColumnIndexOrThrow(UserEntry._ID)));
                user.setAdmin(c.getInt(c.getColumnIndexOrThrow(UserEntry.COLUMN_IS_ADMIN)));
                user.setUsername(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USERNAME)));
                user.setLastLogin(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_LAST_LOGIN)));
                user.setPassword(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_PASSWORD)));
            }
            return user;
        }
    }

    public static List<UserModel> getAll(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + UserEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<UserModel> users = new ArrayList<>();

            while(c.moveToNext()){
                UserModel user = new UserModel();
                user.setUserId(c.getInt(c.getColumnIndexOrThrow(UserEntry._ID)));
                user.setAdmin(c.getInt(c.getColumnIndexOrThrow(UserEntry.COLUMN_IS_ADMIN)));
                user.setUsername(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USERNAME)));
                user.setLastLogin(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_LAST_LOGIN)));
                user.setPassword(c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_PASSWORD)));
                users.add(user);
            }

            return users;
        }
    }
}
