package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.List;
import liftinggoals.models.RoutineModel;

public class RoutineTable{

    public static final String SQL_CREATE_ROUTINE_TABLE = "CREATE TABLE " +
            RoutineEntry.TABLE_NAME + " (" +
            RoutineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RoutineEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
            RoutineEntry.COLUMN_ROUTINE_NAME + " TEXT NOT NULL, " +
            RoutineEntry.COLUMN_DESCRIPTION + " TEXT, " +
            RoutineEntry.COLUMN_NUMBER_WORKOUTS + " TEXT, " +
            RoutineEntry.COLUMN_DEFAULT_ROUTINE + " INTEGER NOT NULL DEFAULT 0" +
            ");";
    public static final String SQL_DROP_ROUTINE_TABLE = "DROP TABLE IF EXISTS " + RoutineEntry.TABLE_NAME;


    public static abstract class RoutineEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Routines";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_ROUTINE_NAME = "routine_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_NUMBER_WORKOUTS = "number_workouts";
        public static final String COLUMN_DEFAULT_ROUTINE = "default_routine";
    }

    public static long insert(SQLiteDatabase myDB, int routineId, int userId, String name, String description, int workouts, int defaultRoutine)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry._ID, routineId);
        values.put(RoutineEntry.COLUMN_USER_ID, userId);
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);
        values.put(RoutineEntry.COLUMN_NUMBER_WORKOUTS, workouts);
        values.put(RoutineEntry.COLUMN_DEFAULT_ROUTINE, defaultRoutine);

        return myDB.insert(RoutineEntry.TABLE_NAME, null, values);
    }

    public static long insert(SQLiteDatabase myDB, int userId, String name, String description, int workouts, int defaultRoutine)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_USER_ID, userId);
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);
        values.put(RoutineEntry.COLUMN_NUMBER_WORKOUTS, workouts);
        values.put(RoutineEntry.COLUMN_DEFAULT_ROUTINE, defaultRoutine);

        return myDB.insert(RoutineEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int routineId, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);

        return myDB.update(RoutineEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(routineId)});
    }

    public static long update(SQLiteDatabase myDB, int routineId, String name, String description, int numberWorkouts)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);
        values.put(RoutineEntry.COLUMN_NUMBER_WORKOUTS, numberWorkouts);

        return myDB.update(RoutineEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(routineId)});
    }

    public static long delete(SQLiteDatabase myDB, int routineId)
    {
        String where = RoutineEntry._ID + " = ?";

        return myDB.delete(RoutineEntry.TABLE_NAME, where, new String[] {Integer.toString(routineId)});
    }

    public static RoutineModel getRoutine(SQLiteDatabase myDB, int routineId)
    {
        String query = "SELECT * FROM " + RoutineEntry.TABLE_NAME + " WHERE " + RoutineEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(routineId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            RoutineModel routineModel = new RoutineModel();

            while(c.moveToNext()){
                routineModel.setRoutineId(c.getInt(c.getColumnIndexOrThrow(RoutineEntry._ID)));
                routineModel.setUserId(c.getInt(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_USER_ID)));
                routineModel.setRoutineName(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_ROUTINE_NAME)));
                routineModel.setRoutineDescription(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_DESCRIPTION)));
                routineModel.setDefaultRoutine(c.getInt(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_DEFAULT_ROUTINE)));
            }

            return routineModel;
        }
    }

    public static List<RoutineModel> getAll(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + RoutineEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<RoutineModel> routines = new ArrayList<>();

            while(c.moveToNext()){
                RoutineModel routineModel = new RoutineModel();
                routineModel.setRoutineId(c.getInt(c.getColumnIndexOrThrow(RoutineEntry._ID)));
                routineModel.setUserId(c.getInt(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_USER_ID)));
                routineModel.setRoutineName(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_ROUTINE_NAME)));
                routineModel.setRoutineDescription(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_DESCRIPTION)));
                routineModel.setDefaultRoutine(c.getInt(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_DEFAULT_ROUTINE)));
                routines.add(routineModel);
            }

            return routines;
        }
    }


}
