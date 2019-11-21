package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.WorkoutModel;

public class WorkoutsTable {
    public static final String SQL_CREATE_WORKOUT_TABLE= "CREATE TABLE " +
            WorkoutEntry.TABLE_NAME + " (" +
            WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WorkoutEntry.COLUMN_WORKOUT_NAME + " TEXT NOT NULL, " +
            WorkoutEntry.COLUMN_DESCRIPTION + " TEXT, " +
            WorkoutEntry.COLUMN_DURATION + " REAL DEFAULT 0, " +
            WorkoutEntry.COLUMN_NUMBER_EXERCISES + " INTEGER DEFAULT 0" +
            ");";
    public static final String SQL_DROP_WORKOUT_TABLE = "DROP TABLE IF EXISTS " + WorkoutEntry.TABLE_NAME;


    public static abstract class WorkoutEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Workouts";
        public static final String COLUMN_WORKOUT_NAME = "workout_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_NUMBER_EXERCISES = "number_exercises";
    }
    public static long insert(SQLiteDatabase myDB, String name, String description, double duration, int exercises)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, name);
        values.put(WorkoutEntry.COLUMN_DESCRIPTION, description);
        values.put(WorkoutEntry.COLUMN_DURATION, duration);
        values.put(WorkoutEntry.COLUMN_NUMBER_EXERCISES, exercises);

        return myDB.insert(WorkoutEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int workoutId, String name, String description, double duration, int exercises)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, name);
        values.put(WorkoutEntry.COLUMN_DESCRIPTION, description);
        values.put(WorkoutEntry.COLUMN_DURATION, duration);
        values.put(WorkoutEntry.COLUMN_NUMBER_EXERCISES, exercises);

        return myDB.update(WorkoutEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(workoutId)});
    }

    public static long update(SQLiteDatabase myDB, int workoutId, String name, double duration)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, name);
        values.put(WorkoutEntry.COLUMN_DURATION, duration);
        //values.put(WorkoutEntry.COLUMN_NUMBER_EXERCISES, exercises);

        return myDB.update(WorkoutEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(workoutId)});
    }

    public static long update(SQLiteDatabase myDB, int workoutId, String name)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, name);

        return myDB.update(WorkoutEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(workoutId)});
    }

    public static long delete(SQLiteDatabase myDB, int id)
    {
        String where = WorkoutEntry._ID + " = " + id;

        return myDB.delete(WorkoutEntry.TABLE_NAME, where, null);
    }

    public static WorkoutModel getWorkout(SQLiteDatabase myDB, int id)
    {
        String query = "SELECT * FROM " + WorkoutEntry.TABLE_NAME + " WHERE " + WorkoutEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(id)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            WorkoutModel workoutModel = new WorkoutModel();

            while(c.moveToNext()){
                workoutModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(WorkoutEntry._ID)));
                workoutModel.setWorkoutName(c.getString(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_WORKOUT_NAME)));
                workoutModel.setDescription(c.getString(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_DESCRIPTION)));
                workoutModel.setEstimatedDuration(c.getDouble(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_DURATION)));
                workoutModel.setNumberExercises(c.getInt(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_NUMBER_EXERCISES)));
            }

            return workoutModel;
        }
    }

    public static List<WorkoutModel> getAllWorkouts(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + WorkoutEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<WorkoutModel> workouts = new ArrayList<>();

            while(c.moveToNext()){
                WorkoutModel workoutModel = new WorkoutModel();
                workoutModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(WorkoutEntry._ID)));
                workoutModel.setWorkoutName(c.getString(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_WORKOUT_NAME)));
                workoutModel.setDescription(c.getString(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_DESCRIPTION)));
                workoutModel.setEstimatedDuration(c.getDouble(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_DURATION)));
                workoutModel.setNumberExercises(c.getInt(c.getColumnIndexOrThrow(WorkoutEntry.COLUMN_NUMBER_EXERCISES)));
                workouts.add(workoutModel);
            }

            return workouts;
        }
    }

}
