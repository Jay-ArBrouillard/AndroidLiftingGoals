package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RoutineModel;

public class ExercisesTable {

    public static final String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " +
            ExerciseEntry.TABLE_NAME + " (" +
            ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ExerciseEntry.COLUMN_EXERCISE_NAME + " TEXT" +
            ");";
    public static final String SQL_DROP_EXERCISE_TABLE = "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME;


    public static abstract class ExerciseEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Exercises";
        public static final String COLUMN_EXERCISE_NAME = "exercise_name";
    }
    public static long insert(SQLiteDatabase myDB, String name)
    {
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_EXERCISE_NAME, name);

        return myDB.insert(ExerciseEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, String name)
    {
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_EXERCISE_NAME, name);

        return myDB.update(ExerciseEntry.TABLE_NAME, values, null, null);
    }

    public static long delete(SQLiteDatabase myDB, String name)
    {
        String where = ExerciseEntry.COLUMN_EXERCISE_NAME + " = " + name;

        return myDB.delete(ExerciseEntry.TABLE_NAME, where, null);
    }

    public static ExerciseModel getExercise(SQLiteDatabase myDB, int exerciseId)
    {
        String query = "SELECT * FROM " + ExerciseEntry.TABLE_NAME + " WHERE " + ExerciseEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(exerciseId)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ExerciseModel exerciseModel = new ExerciseModel();

            while(c.moveToNext()){
                exerciseModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(ExerciseEntry._ID)));
                exerciseModel.setExerciseName(c.getString(c.getColumnIndexOrThrow(ExerciseEntry.COLUMN_EXERCISE_NAME)));
            }

            return exerciseModel;
        }
    }

    public static List<ExerciseModel> getAllExercises(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + ExerciseEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<ExerciseModel> exercises = new ArrayList<>();

            while(c.moveToNext()){
                ExerciseModel exerciseModel = new ExerciseModel();
                exerciseModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(ExerciseEntry._ID)));
                exerciseModel.setExerciseName(c.getString(c.getColumnIndexOrThrow(ExerciseEntry.COLUMN_EXERCISE_NAME)));
                exercises.add(exerciseModel);
            }

            return exercises;
        }
    }
}
