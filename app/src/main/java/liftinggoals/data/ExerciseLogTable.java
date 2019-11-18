package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.ExerciseLogModel;
import liftinggoals.classes.ExerciseModel;

public class ExerciseLogTable {

    public static final String SQL_CREATE_EXERCISE_LOG_TABLE = "CREATE TABLE " +
            ExerciseLogEntry.TABLE_NAME + " (" +
            ExerciseLogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ExerciseLogEntry.COLUMN_WORKOUT_EXERCISE_ID + " INTEGER NOT NULL, " +
            ExerciseLogEntry.COLUMN_SET_PERFORMED + " INTEGER, " +
            ExerciseLogEntry.COLUMN_REPS_PERFORMED + " INTEGER, " +
            ExerciseLogEntry.COLUMN_INTENSITY + " REAL, " +
            ExerciseLogEntry.COLUMN_RPE + " REAL, " +
            ExerciseLogEntry.COLUMN_REST_DURATION + " REAL, " +
            ExerciseLogEntry.COLUMN_TEMPO + " TEXT, " +
            ExerciseLogEntry.COLUMN_DATE_PERFORMED + " TEXT" +
            ");";
    public static final String SQL_DROP_EXERCISE_LOG_TABLE = "DROP TABLE IF EXISTS " + ExerciseLogEntry.TABLE_NAME;

    public static abstract class ExerciseLogEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "ExerciseLog";
        public static final String COLUMN_WORKOUT_EXERCISE_ID = "workout_exercise_id";
        public static final String COLUMN_SET_PERFORMED = "set_performed";
        public static final String COLUMN_REPS_PERFORMED = "reps_performed";
        public static final String COLUMN_INTENSITY = "intensity";
        public static final String COLUMN_RPE = "rating_perceived_exertion";
        public static final String COLUMN_REST_DURATION = "rest_duration";
        public static final String COLUMN_TEMPO = "tempo";
        public static final String COLUMN_DATE_PERFORMED = "date_performed";
    }
    public static long insert(SQLiteDatabase myDB, ExerciseLogModel entity)
    {
        ContentValues values = new ContentValues();
        values.put(ExerciseLogEntry.COLUMN_WORKOUT_EXERCISE_ID, entity.getWorkoutExeriseId());
        values.put(ExerciseLogEntry.COLUMN_SET_PERFORMED, entity.getSetPerformed());
        values.put(ExerciseLogEntry.COLUMN_REPS_PERFORMED, entity.getRepsPerformed());
        values.put(ExerciseLogEntry.COLUMN_INTENSITY, entity.getIntensity());
        //values.put(ExerciseLogEntry.COLUMN_RPE, entity.getRpe());
        //values.put(ExerciseLogEntry.COLUMN_REST_DURATION, entity.getRestDuration());
        //values.put(ExerciseLogEntry.COLUMN_TEMPO, entity.getTempo());
        values.put(ExerciseLogEntry.COLUMN_DATE_PERFORMED, entity.getDate());

        return myDB.insert(ExerciseLogEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, String name)
    {
        ContentValues values = new ContentValues();
        //values.put(ExerciseLogEntry.COLUMN_EXERCISE_NAME, name);

        return myDB.update(ExerciseLogEntry.TABLE_NAME, values, "exercise_name = ?", new String[] {name});
    }

    public static long delete(SQLiteDatabase myDB, String name)
    {
        //String where = ExerciseLogEntry.COLUMN_EXERCISE_NAME + " = " + name;
        String where = null;
        return myDB.delete(ExerciseLogEntry.TABLE_NAME, where, null);
    }

    public static List<ExerciseLogModel> getExercisesLogsByWorkoutExerciseId(SQLiteDatabase myDB, int id)
    {
        String query = "SELECT * FROM " + ExerciseLogEntry.TABLE_NAME + " WHERE " + ExerciseLogEntry.COLUMN_WORKOUT_EXERCISE_ID + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(id)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<ExerciseLogModel> exercises = new ArrayList<>();

            while(c.moveToNext()){
                ExerciseLogModel exerciseLogModel = new ExerciseLogModel();
                exerciseLogModel.setUserExerciseLogId(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry._ID)));
                exerciseLogModel.setWorkoutExeriseId(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_WORKOUT_EXERCISE_ID)));
                exerciseLogModel.setSetPerformed(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_SET_PERFORMED)));
                exerciseLogModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_REPS_PERFORMED)));
                exerciseLogModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_INTENSITY)));
                exerciseLogModel.setRpe(c.getDouble(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_RPE)));
                exerciseLogModel.setRestDuration(c.getDouble(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_REST_DURATION)));
                exerciseLogModel.setTempo(c.getString(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_TEMPO)));
                exerciseLogModel.setDate(c.getString(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_DATE_PERFORMED)));
                exercises.add(exerciseLogModel);
            }

            return exercises;
        }
    }




    public static List<ExerciseLogModel> getAllExercisesLogs(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + ExerciseLogEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<ExerciseLogModel> exercises = new ArrayList<>();

            while(c.moveToNext()){
                ExerciseLogModel exerciseLogModel = new ExerciseLogModel();
                exerciseLogModel.setUserExerciseLogId(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry._ID)));
                exerciseLogModel.setWorkoutExeriseId(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_WORKOUT_EXERCISE_ID)));
                exerciseLogModel.setSetPerformed(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_SET_PERFORMED)));
                exerciseLogModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_REPS_PERFORMED)));
                exerciseLogModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_INTENSITY)));
                exerciseLogModel.setRpe(c.getDouble(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_RPE)));
                exerciseLogModel.setRestDuration(c.getDouble(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_REST_DURATION)));
                exerciseLogModel.setTempo(c.getString(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_TEMPO)));
                exerciseLogModel.setDate(c.getString(c.getColumnIndexOrThrow(ExerciseLogEntry.COLUMN_DATE_PERFORMED)));
                exercises.add(exerciseLogModel);
            }

            return exercises;
        }
    }
}
