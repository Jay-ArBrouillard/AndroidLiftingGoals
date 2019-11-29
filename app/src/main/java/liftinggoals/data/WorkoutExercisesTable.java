package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.models.WorkoutExerciseModel;

public class WorkoutExercisesTable {

    public static final String SQL_CREATE_WORKOUT_EXERCISES_TABLE = "CREATE TABLE " +
            WorkoutExercisesEntry.TABLE_NAME + " (" +
            WorkoutExercisesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WorkoutExercisesEntry.COLUMN_WORKOUT_ID + " INTEGER NOT NULL, " +
            WorkoutExercisesEntry.COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
            WorkoutExercisesEntry.COLUMN_MINIMUM_SETS + " INTEGER, " +
            WorkoutExercisesEntry.COLUMN_MINIMUM_REPS + " INTEGER, " +
            WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS + " INTEGER, " +
            WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS + " INTEGER, " +
            WorkoutExercisesEntry.COLUMN_INTENSITY + " REAL" +
            ");";
    public static final String SQL_DROP_WORKOUT_EXERCISES_TABLE = "DROP TABLE IF EXISTS " + WorkoutExercisesEntry.TABLE_NAME;


    public static abstract class WorkoutExercisesEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "WorkoutExercises";
        public static final String COLUMN_WORKOUT_ID = "workout_id";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_MINIMUM_SETS = "minimum_sets";
        public static final String COLUMN_MINIMUM_REPS = "minimum_reps";
        public static final String COLUMN_MAXIMUM_SETS = "maximum_sets";
        public static final String COLUMN_MAXIMUM_REPS = "maximum_reps";
        public static final String COLUMN_INTENSITY = "intensity";
    }

    public static long insert(SQLiteDatabase myDB, int workoutExerciseId, int workoutId, int exerciseId,
                              int minimumSets, int minimumReps, int maximumSets, int maximumReps, double intensity)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutExercisesEntry._ID, workoutExerciseId);
        values.put(WorkoutExercisesEntry.COLUMN_WORKOUT_ID, workoutId);
        values.put(WorkoutExercisesEntry.COLUMN_EXERCISE_ID, exerciseId);
        values.put(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS, minimumSets);
        values.put(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS, minimumReps);
        values.put(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS, maximumSets);
        values.put(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS, maximumReps);
        values.put(WorkoutExercisesEntry.COLUMN_INTENSITY, intensity);

        return myDB.insert(WorkoutExercisesEntry.TABLE_NAME, null, values);
    }

    public static long insert(SQLiteDatabase myDB, int workoutId, int exerciseId,
                              int minimumSets, int minimumReps, int maximumSets, int maximumReps, double intensity)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutExercisesEntry.COLUMN_WORKOUT_ID, workoutId);
        values.put(WorkoutExercisesEntry.COLUMN_EXERCISE_ID, exerciseId);
        values.put(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS, minimumSets);
        values.put(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS, minimumReps);
        values.put(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS, maximumSets);
        values.put(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS, maximumReps);
        values.put(WorkoutExercisesEntry.COLUMN_INTENSITY, intensity);

        return myDB.insert(WorkoutExercisesEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int workoutExerciseId, int minimumSets, int minimumReps, int maximumSets, int maximumReps, double intensity)
    {
        ContentValues values = new ContentValues();
        values.put(WorkoutExercisesEntry._ID, workoutExerciseId);
        if (minimumSets != -1)
            values.put(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS, minimumSets);
        if (minimumReps != -1)
            values.put(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS, minimumReps);
        if (maximumSets != -1)
            values.put(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS, maximumSets);
        if (maximumReps != -1)
            values.put(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS, maximumReps);
        if (intensity != -1)
            values.put(WorkoutExercisesEntry.COLUMN_INTENSITY, intensity);

        return myDB.update(WorkoutExercisesEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(workoutExerciseId)});
    }

    public static long delete(SQLiteDatabase myDB, int id)
    {
        String where = WorkoutExercisesEntry._ID + " = " + id;

        return myDB.delete(WorkoutExercisesEntry.TABLE_NAME, where, null);
    }

    public static WorkoutExerciseModel getWorkoutExercise(SQLiteDatabase myDB, int id)
    {
        String query = "SELECT * FROM " + WorkoutExercisesEntry.TABLE_NAME + " WHERE " + WorkoutExercisesEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(id)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();

            while(c.moveToNext()){
                workoutExerciseModel.setWorkoutExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry._ID)));
                workoutExerciseModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_WORKOUT_ID)));
                workoutExerciseModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_EXERCISE_ID)));
                workoutExerciseModel.setMinimumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS)));
                workoutExerciseModel.setMinimumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS)));
                workoutExerciseModel.setMaximumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS)));
                workoutExerciseModel.setMaximumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS)));
                workoutExerciseModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_INTENSITY)));
            }

            return workoutExerciseModel;
        }
    }

    public static WorkoutExerciseModel getWorkoutExercise(SQLiteDatabase myDB, int workoutId, int exerciseId)
    {
        String query = "SELECT * FROM " + WorkoutExercisesEntry.TABLE_NAME + " WHERE " + WorkoutExercisesEntry.COLUMN_WORKOUT_ID  + " = ? AND " + WorkoutExercisesEntry.COLUMN_EXERCISE_ID + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(workoutId), Integer.toString(exerciseId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();

            while(c.moveToNext()){
                workoutExerciseModel.setWorkoutExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry._ID)));
                workoutExerciseModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_WORKOUT_ID)));
                workoutExerciseModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_EXERCISE_ID)));
                workoutExerciseModel.setMinimumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS)));
                workoutExerciseModel.setMinimumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS)));
                workoutExerciseModel.setMaximumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS)));
                workoutExerciseModel.setMaximumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS)));
                workoutExerciseModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_INTENSITY)));
            }

            return workoutExerciseModel;
        }
    }

    public static List<WorkoutExerciseModel> getAllWorkoutExercisesByWorkoutId(SQLiteDatabase myDB, int workoutId)
    {
        String query = "SELECT * FROM " + WorkoutExercisesEntry.TABLE_NAME + " WHERE " + WorkoutExercisesEntry.COLUMN_WORKOUT_ID + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(workoutId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<WorkoutExerciseModel> workoutExercises = new ArrayList<>();

            while(c.moveToNext()){
                WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();
                workoutExerciseModel.setWorkoutExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry._ID)));
                workoutExerciseModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_WORKOUT_ID)));
                workoutExerciseModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_EXERCISE_ID)));
                workoutExerciseModel.setMinimumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS)));
                workoutExerciseModel.setMinimumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS)));
                workoutExerciseModel.setMaximumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS)));
                workoutExerciseModel.setMaximumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS)));
                workoutExerciseModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_INTENSITY)));
                workoutExercises.add(workoutExerciseModel);
            }

            return workoutExercises;
        }
    }

    public static List<Integer> getAllWorkoutExerciseIdsByExerciseId(SQLiteDatabase myDB, int exerciseId)
    {
        //List<WorkoutExerciseModel> temp = getAllWorkoutExercises(myDB);

        String query = "SELECT " + WorkoutExercisesEntry._ID + " FROM " + WorkoutExercisesEntry.TABLE_NAME + " WHERE " + WorkoutExercisesEntry.COLUMN_EXERCISE_ID + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(exerciseId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<Integer> workoutExerciseIds = new ArrayList<>();

            while(c.moveToNext()){
                workoutExerciseIds.add(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry._ID)));
            }

            return workoutExerciseIds;
        }
    }

    public static List<WorkoutExerciseModel> getAllWorkoutExercises(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + WorkoutExercisesEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<WorkoutExerciseModel> workoutExercises = new ArrayList<>();

            while(c.moveToNext()){
                WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();
                workoutExerciseModel.setWorkoutExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry._ID)));
                workoutExerciseModel.setWorkoutId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_WORKOUT_ID)));
                workoutExerciseModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_EXERCISE_ID)));
                workoutExerciseModel.setMinimumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_SETS)));
                workoutExerciseModel.setMinimumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MINIMUM_REPS)));
                workoutExerciseModel.setMaximumSets(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_SETS)));
                workoutExerciseModel.setMaximumReps(c.getInt(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_MAXIMUM_REPS)));
                workoutExerciseModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(WorkoutExercisesEntry.COLUMN_INTENSITY)));
                workoutExercises.add(workoutExerciseModel);
            }

            return workoutExercises;
        }
    }
}
