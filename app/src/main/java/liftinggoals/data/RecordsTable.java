package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RecordModel;

public class RecordsTable {

    public static final String SQL_CREATE_RECORDS_TABLE = "CREATE TABLE " +
            RecordEntry.TABLE_NAME + " (" +
            RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RecordEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
            RecordEntry.COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
            RecordEntry.COLUMN_INTENSITY + " REAL DEFAULT 0, " +
            RecordEntry.COLUMN_REPS_PERFORMED + " INTEGER DEFAULT 0" +

            ");";
    public static final String SQL_DROP_RECORDS_TABLE = "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;


    public static abstract class RecordEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Records";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_INTENSITY = "intensity";
        public static final String COLUMN_REPS_PERFORMED = "reps_performed";
    }

    public static long insert(SQLiteDatabase myDB, int userId, int exercise_id, double intensity, int reps)
    {
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_USER_ID, userId);
        values.put(RecordEntry.COLUMN_EXERCISE_ID, exercise_id);
        values.put(RecordEntry.COLUMN_INTENSITY, intensity);
        values.put(RecordEntry.COLUMN_REPS_PERFORMED, reps);

        return myDB.insert(RecordEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int userId, int exercise_id, double intensity, int reps)
    {
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_USER_ID, userId);
        values.put(RecordEntry.COLUMN_EXERCISE_ID, exercise_id);
        values.put(RecordEntry.COLUMN_INTENSITY, intensity);
        values.put(RecordEntry.COLUMN_REPS_PERFORMED, reps);

        return myDB.update(RecordEntry.TABLE_NAME, values, null, null);
    }

    public static long delete(SQLiteDatabase myDB, int id)
    {
        String where = RecordEntry._ID + " = " + id;

        return myDB.delete(RecordEntry.TABLE_NAME, where, null);
    }

    public static RecordModel getRecord(SQLiteDatabase myDB, int id)
    {
        String query = "SELECT * FROM " + RecordEntry.TABLE_NAME + " WHERE " + RecordEntry._ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(id)});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            RecordModel recordModel = new RecordModel();

            while(c.moveToNext()){
                recordModel.setRecordId(c.getInt(c.getColumnIndexOrThrow(RecordEntry._ID)));
                recordModel.setUserId(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_USER_ID)));
                recordModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_EXERCISE_ID)));
                recordModel.setIntensity(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_INTENSITY)));
                recordModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_REPS_PERFORMED)));
            }

            return recordModel;
        }
    }

    public static List<RecordModel> getAllRecords(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + RecordEntry.TABLE_NAME;

        Cursor c = myDB.rawQuery(query, null);

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<RecordModel> records = new ArrayList<>();

            while(c.moveToNext()){
                RecordModel recordModel = new RecordModel();
                recordModel.setRecordId(c.getInt(c.getColumnIndexOrThrow(RecordEntry._ID)));
                recordModel.setUserId(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_USER_ID)));
                recordModel.setExerciseId(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_EXERCISE_ID)));
                recordModel.setIntensity(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_INTENSITY)));
                recordModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_REPS_PERFORMED)));
                records.add(recordModel);
            }

            return records;
        }
    }
}
