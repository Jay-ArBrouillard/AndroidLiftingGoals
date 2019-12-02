package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;
import liftinggoals.models.UserRoutineModel;

public class UserRoutinesTable {

    public static final String SQL_CREATE_USER_ROUTINE_TABLE = "CREATE TABLE " +
            UserRoutineEntry.TABLE_NAME + " (" +
            UserRoutineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserRoutineEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
            UserRoutineEntry.COLUMN_ROUTINE_ID + " INTEGER NOT NULL" +
            ");";
    public static final String SQL_DROP_USER_ROUTINE_TABLE = "DROP TABLE IF EXISTS " + UserRoutineEntry.TABLE_NAME;


    public static abstract class UserRoutineEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "UserRoutines";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_ROUTINE_ID = "routine_id";
    }

    public static long insert(SQLiteDatabase myDB, int userRoutineId, int userId, int routineId)
    {
        ContentValues values = new ContentValues();
        values.put(UserRoutineEntry._ID, userRoutineId);
        values.put(UserRoutineEntry.COLUMN_USER_ID, userId);
        values.put(UserRoutineEntry.COLUMN_ROUTINE_ID, routineId);

        return myDB.insert(UserRoutineEntry.TABLE_NAME, null, values);
    }

    public static long insert(SQLiteDatabase myDB,int userId, int routineId)
    {
        ContentValues values = new ContentValues();
        values.put(UserRoutineEntry.COLUMN_USER_ID, userId);
        values.put(UserRoutineEntry.COLUMN_ROUTINE_ID, routineId);

        return myDB.insert(UserRoutineEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int userRoutineId, int userId, int routineId)
    {
        ContentValues values = new ContentValues();
        values.put(UserRoutineEntry.COLUMN_USER_ID, userId);
        values.put(UserRoutineEntry.COLUMN_ROUTINE_ID, routineId);

        return myDB.update(UserRoutineEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(userRoutineId)});
    }

    public static long delete(SQLiteDatabase myDB, int userRoutineId)
    {
        String where = UserRoutineEntry._ID + " = " + userRoutineId;

        return myDB.delete(UserRoutineEntry.TABLE_NAME, where, null);
    }

    public static UserRoutineModel get(SQLiteDatabase myDB, int userRoutineId)
    {
        String query = "SELECT * FROM " + UserRoutineEntry.TABLE_NAME + " WHERE " + UserRoutineEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(userRoutineId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            UserRoutineModel userRoutine = new UserRoutineModel();

            while(c.moveToNext()){
                userRoutine.setUserRoutineId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry._ID)));
                userRoutine.setUserId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry.COLUMN_USER_ID)));
                userRoutine.setRoutineId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry.COLUMN_ROUTINE_ID)));
            }

            return userRoutine;
        }
    }

    public static UserRoutineModel get(SQLiteDatabase myDB, int userId, int routineId)
    {
        String query = "SELECT * FROM " + UserRoutineEntry.TABLE_NAME + " WHERE " + UserRoutineEntry.COLUMN_USER_ID  + " = ? AND " + UserRoutineEntry.COLUMN_ROUTINE_ID + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(userId), Integer.toString(routineId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            UserRoutineModel userRoutine = new UserRoutineModel();

            while(c.moveToNext()){
                userRoutine.setUserRoutineId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry._ID)));
                userRoutine.setUserId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry.COLUMN_USER_ID)));
                userRoutine.setRoutineId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry.COLUMN_ROUTINE_ID)));
            }

            return userRoutine;
        }
    }

    public static List<UserRoutineModel> getAll(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + UserRoutineEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<UserRoutineModel> userRoutines = new ArrayList<>();

            while(c.moveToNext()){
                UserRoutineModel userRoutine = new UserRoutineModel();
                userRoutine.setUserRoutineId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry._ID)));
                userRoutine.setUserId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry.COLUMN_USER_ID)));
                userRoutine.setRoutineId(c.getInt(c.getColumnIndexOrThrow(UserRoutineEntry.COLUMN_ROUTINE_ID)));
                userRoutines.add(userRoutine);
            }

            return userRoutines;
        }
    }
}
