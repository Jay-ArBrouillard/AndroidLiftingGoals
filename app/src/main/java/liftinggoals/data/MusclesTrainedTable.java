package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.MusclesTrainedModel;

public class MusclesTrainedTable {
    public static final String SQL_CREATE_MUSCLES_TRAINED_TABLE = "CREATE TABLE " +
            MusclesTrainedEntry.TABLE_NAME + " (" +
            MusclesTrainedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MusclesTrainedEntry.COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
            MusclesTrainedEntry.COLUMN_MUSCLE_GROUP + " TEXT NOT NULL" +
            ");";
    public static final String SQL_DROP_MUSCLES_TRAINED_TABLE = "DROP TABLE IF EXISTS " + MusclesTrainedEntry.TABLE_NAME;

    public static abstract class MusclesTrainedEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "MusclesTrained";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_MUSCLE_GROUP = "muscle_group";
    }
    public static long insert(SQLiteDatabase myDB, int exerciseId, String muscleGroup)
    {
        ContentValues values = new ContentValues();
        values.put(MusclesTrainedEntry.COLUMN_EXERCISE_ID, exerciseId);
        values.put(MusclesTrainedEntry.COLUMN_MUSCLE_GROUP, muscleGroup);

        return myDB.insert(MusclesTrainedEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int id, int exerciseId, String muscleGroup)
    {
        ContentValues values = new ContentValues();
        values.put(MusclesTrainedEntry.COLUMN_EXERCISE_ID, exerciseId);
        values.put(MusclesTrainedEntry.COLUMN_MUSCLE_GROUP, muscleGroup);

        return myDB.update(MusclesTrainedEntry.TABLE_NAME, values, "_id = ?", new String[] {Integer.toString(id)});
    }

    public static long delete(SQLiteDatabase myDB, int id)
    {
        String where = MusclesTrainedEntry._ID + " = " + id;

        return myDB.delete(MusclesTrainedEntry.TABLE_NAME, where, null);
    }

    public static List<MusclesTrainedModel> getAll(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + MusclesTrainedEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<MusclesTrainedModel> musclesTrainedModels = new ArrayList<>();

            while(c.moveToNext()){
                MusclesTrainedModel musclesTrainedModel = new MusclesTrainedModel();
                musclesTrainedModel.setMusclesTrainedId(c.getInt(c.getColumnIndexOrThrow(MusclesTrainedEntry._ID)));
                musclesTrainedModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(MusclesTrainedEntry.COLUMN_EXERCISE_ID)));
                musclesTrainedModel.setMuscleGroup(c.getString(c.getColumnIndexOrThrow(MusclesTrainedEntry.COLUMN_MUSCLE_GROUP)));
                musclesTrainedModels.add(musclesTrainedModel);
            }

            return musclesTrainedModels;
        }
    }
}
