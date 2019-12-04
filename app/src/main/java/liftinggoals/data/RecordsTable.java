package liftinggoals.data;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.models.RecordModel;

public class RecordsTable {

    public static final String SQL_CREATE_RECORDS_TABLE = "CREATE TABLE " +
            RecordEntry.TABLE_NAME + " (" +
            RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RecordEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
            RecordEntry.COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
            RecordEntry.COLUMN_INTENSITY + " REAL DEFAULT 0, " +
            RecordEntry.COLUMN_REPS_PERFORMED + " INTEGER DEFAULT 0, " +
            RecordEntry.COLUMN_DATE_PERFORMED + " TEXT NOT NULL" +
            ");";
    public static final String SQL_DROP_RECORDS_TABLE = "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;


    public static abstract class RecordEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Records";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_INTENSITY = "intensity";
        public static final String COLUMN_REPS_PERFORMED = "reps_performed";
        public static final String COLUMN_DATE_PERFORMED = "date_performed";
    }

    public static long insert(SQLiteDatabase myDB, int userId, int exercise_id, double intensity, int reps, String date)
    {
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_USER_ID, userId);
        values.put(RecordEntry.COLUMN_EXERCISE_ID, exercise_id);
        values.put(RecordEntry.COLUMN_INTENSITY, intensity);
        values.put(RecordEntry.COLUMN_REPS_PERFORMED, reps);
        values.put(RecordEntry.COLUMN_DATE_PERFORMED, date);

        return myDB.insert(RecordEntry.TABLE_NAME, null, values);
    }

    public static long insert(SQLiteDatabase myDB, int recordId, RecordModel entity)
    {
        ContentValues values = new ContentValues();
        values.put(RecordEntry._ID, recordId);
        values.put(RecordEntry.COLUMN_USER_ID, entity.getUserId());
        values.put(RecordEntry.COLUMN_EXERCISE_ID, entity.getExerciseId());
        values.put(RecordEntry.COLUMN_INTENSITY, entity.getIntensity());
        values.put(RecordEntry.COLUMN_REPS_PERFORMED, entity.getRepsPerformed());
        values.put(RecordEntry.COLUMN_DATE_PERFORMED, entity.getDate());

        return myDB.insert(RecordEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, int userId, int exercise_id, double intensity, int reps, String date)
    {
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_USER_ID, userId);
        values.put(RecordEntry.COLUMN_EXERCISE_ID, exercise_id);
        values.put(RecordEntry.COLUMN_INTENSITY, intensity);
        values.put(RecordEntry.COLUMN_REPS_PERFORMED, reps);
        values.put(RecordEntry.COLUMN_DATE_PERFORMED, date);

        String where = RecordEntry.COLUMN_USER_ID + " = ? AND " + RecordEntry.COLUMN_REPS_PERFORMED + " = ? AND " + RecordEntry.COLUMN_EXERCISE_ID + " =  ?";

        return myDB.update(RecordEntry.TABLE_NAME, values, where, new String[] {Integer.toString(userId), Integer.toString(reps), Integer.toString(exercise_id)});
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
                recordModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(RecordEntry.COLUMN_INTENSITY)));
                recordModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_REPS_PERFORMED)));
                recordModel.setDate(c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_DATE_PERFORMED)));
            }

            return recordModel;
        }
    }

    public static List<RecordModel> getRecordsByExerciseId(SQLiteDatabase myDB, int exerciseId)
    {
        String query = "SELECT * FROM " + RecordEntry.TABLE_NAME + " WHERE  " + RecordEntry.COLUMN_EXERCISE_ID  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {Integer.toString(exerciseId)});

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
                recordModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(RecordEntry.COLUMN_INTENSITY)));
                recordModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_REPS_PERFORMED)));
                recordModel.setDate(c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_DATE_PERFORMED)));
                records.add(recordModel);
            }

            return records;
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
                recordModel.setIntensity(c.getDouble(c.getColumnIndexOrThrow(RecordEntry.COLUMN_INTENSITY)));
                recordModel.setRepsPerformed(c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_REPS_PERFORMED)));
                recordModel.setDate(c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_DATE_PERFORMED)));
                records.add(recordModel);
            }

            return records;
        }
    }
}
