package liftinggoals.data;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.RoutineWorkoutModel;

public class RoutineWorkoutsTable {

    public static final String SQL_CREATE_ROUTINE_WORKOUTS_TABLE = "CREATE TABLE " +
            RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME + " (" +
            RoutineWorkoutsTable.RoutineWorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID + " INTEGER NOT NULL, " +
            RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID + " INTEGER NOT NULL" +
            ");";
    public static final String SQL_DROP_ROUTINE_WORKOUTS_TABLE = "DROP TABLE IF EXISTS " + RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME;


    public static abstract class RoutineWorkoutEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "RoutineWorkouts";
        public static final String COLUMN_ROUTINE_ID = "routine_id";
        public static final String COLUMN_WORKOUT_ID = "workout_id";
    }
    public static long insert(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID, routineId);
        values.put(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID, workoutId);

        return myDB.insert(RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID, routineId);
        values.put(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID, workoutId);

        return myDB.update(RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME, values, null, null);
    }

    public static long delete(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        String where = RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID + " = " + routineId + " AND " + RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID + " = " + workoutId;

        return myDB.delete(RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME, where, null);
    }

    public static RoutineWorkoutModel getRoutineWorkout(SQLiteDatabase myDB, int routineId, int workoutId)
    {
        String query = "SELECT * FROM " + RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME + " WHERE " + RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID  + " = ? AND " + RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(routineId), Integer.toString(workoutId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            RoutineWorkoutModel routineWorkoutModel = new RoutineWorkoutModel();

            while(c.moveToNext()){
                routineWorkoutModel.setRoutine_workout_id(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry._ID)));
                routineWorkoutModel.setRoutine_id(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID)));
                routineWorkoutModel.setWorkout_id(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID)));
            }

            return routineWorkoutModel;
        }
    }

    public static List<RoutineWorkoutModel> getAllRoutines(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + RoutineWorkoutsTable.RoutineWorkoutEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<RoutineWorkoutModel> routines = new ArrayList<>();

            while(c.moveToNext()){
                RoutineWorkoutModel routineWorkoutModel = new RoutineWorkoutModel();
                routineWorkoutModel.setRoutine_workout_id(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutEntry._ID)));
                routineWorkoutModel.setRoutine_id(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_ROUTINE_ID)));
                routineWorkoutModel.setWorkout_id(c.getInt(c.getColumnIndexOrThrow(RoutineWorkoutsTable.RoutineWorkoutEntry.COLUMN_WORKOUT_ID)));
                routines.add(routineWorkoutModel);
            }

            return routines;
        }
    }

}
