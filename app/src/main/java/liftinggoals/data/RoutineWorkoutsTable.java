package liftinggoals.data;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.List;
import liftinggoals.classes.RoutineWorkoutModel;

public class RoutineWorkoutsTable {

    public static final String SQL_CREATE_ROUTINE_WORKOUTS_TABLE = "CREATE TABLE " +
            RoutineWorkoutEntry.TABLE_NAME + " (" +
            RoutineWorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RoutineWorkoutEntry.COLUMN_ROUTINE_ID + " INTEGER NOT NULL, " +
            RoutineWorkoutEntry.COLUMN_WORKOUT_ID + " INTEGER NOT NULL" +
            ");";
    public static final String SQL_DROP_ROUTINE_WORKOUTS_TABLE = "DROP TABLE IF EXISTS " + RoutineWorkoutEntry.TABLE_NAME;


    public static abstract class RoutineWorkoutEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "RoutineWorkouts";
        public static final String COLUMN_ROUTINE_ID = "routine_id";
        public static final String COLUMN_WORKOUT_ID = "workout_id";
    }
    public static long insert(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineWorkoutEntry.COLUMN_ROUTINE_ID, routineId);
        values.put(RoutineWorkoutEntry.COLUMN_WORKOUT_ID, workoutId);

        return myDB.insert(RoutineWorkoutEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int routineWorkoutId, int routineId, int workoutId)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineWorkoutEntry.COLUMN_ROUTINE_ID, routineId);
        values.put(RoutineWorkoutEntry.COLUMN_WORKOUT_ID, workoutId);

        return myDB.update(RoutineWorkoutEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(routineWorkoutId)});
    }

    public static long delete(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        String where = RoutineWorkoutEntry.COLUMN_ROUTINE_ID + " = " + routineId + " AND " + RoutineWorkoutEntry.COLUMN_WORKOUT_ID + " = " + workoutId;

        return myDB.delete(RoutineWorkoutEntry.TABLE_NAME, where, null);
    }

    public static RoutineWorkoutModel getRoutineWorkout(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        String query = "SELECT * FROM " + RoutineWorkoutEntry.TABLE_NAME + " WHERE " + RoutineWorkoutEntry.COLUMN_ROUTINE_ID  + " = ? AND " + RoutineWorkoutEntry.COLUMN_WORKOUT_ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(routineId), Integer.toString(workoutId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            RoutineWorkoutModel routineWorkoutModel = new RoutineWorkoutModel();

            while(c.moveToNext()){
                routineWorkoutModel.setRoutineWorkoutId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry._ID)));
                routineWorkoutModel.setRoutineId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry.COLUMN_ROUTINE_ID)));
                routineWorkoutModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry.COLUMN_WORKOUT_ID)));
            }

            return routineWorkoutModel;
        }
    }

    public static List<RoutineWorkoutModel> getRoutineWorkoutsByRoutineId(SQLiteDatabase myDB, int routineId)
    {
        String query = "SELECT * FROM " + RoutineWorkoutEntry.TABLE_NAME + " WHERE " + RoutineWorkoutEntry.COLUMN_ROUTINE_ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(routineId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<RoutineWorkoutModel> workouts = new ArrayList<>();

            while(c.moveToNext()){
                RoutineWorkoutModel routineWorkoutModel = new RoutineWorkoutModel();
                routineWorkoutModel.setRoutineWorkoutId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry._ID)));
                routineWorkoutModel.setRoutineId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry.COLUMN_ROUTINE_ID)));
                routineWorkoutModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry.COLUMN_WORKOUT_ID)));
                workouts.add(routineWorkoutModel);
            }

            return workouts;
        }
    }

    public static List<RoutineWorkoutModel> getAllRoutineWorkouts(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + RoutineWorkoutEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<RoutineWorkoutModel> workouts = new ArrayList<>();

            while(c.moveToNext()){
                RoutineWorkoutModel routineWorkoutModel = new RoutineWorkoutModel();
                routineWorkoutModel.setRoutineWorkoutId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry._ID)));
                routineWorkoutModel.setRoutineId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry.COLUMN_ROUTINE_ID)));
                routineWorkoutModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry.COLUMN_WORKOUT_ID)));
                workouts.add(routineWorkoutModel);
            }

            return workouts;
        }
    }

}
