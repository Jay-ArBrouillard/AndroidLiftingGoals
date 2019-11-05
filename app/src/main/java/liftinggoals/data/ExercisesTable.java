package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.RoutineModel;

public class ExercisesTable {

    public static final String SQL_CREATE_ROUTINE_TABLE = "CREATE TABLE " +
            RoutineTable.RoutineEntry.TABLE_NAME + " (" +
            RoutineTable.RoutineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME + " TEXT NOT NULL, " +
            RoutineTable.RoutineEntry.COLUMN_DESCRIPTION + " TEXT, " +
            RoutineTable.RoutineEntry.COLUMN_NUMBER_WORKOUTS + " TEXT" +
            ");";
    public static final String SQL_DROP_ROUTINE_TABLE = "DROP TABLE IF EXISTS " + RoutineTable.RoutineEntry.TABLE_NAME;


    public static abstract class ExerciseEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Routines";
        public static final String COLUMN_ROUTINE_NAME = "routine_name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_NUMBER_WORKOUTS = "number_workouts";
    }
    public static long insert(SQLiteDatabase myDB, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineTable.RoutineEntry.COLUMN_DESCRIPTION, description);

        return myDB.insert(RoutineTable.RoutineEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineTable.RoutineEntry.COLUMN_DESCRIPTION, description);

        return myDB.update(RoutineTable.RoutineEntry.TABLE_NAME, values, null, null);
    }

    public static long delete(SQLiteDatabase myDB, String name)
    {
        String where = RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME + " = " + name;

        return myDB.delete(RoutineTable.RoutineEntry.TABLE_NAME, where, null);
    }

    public static RoutineModel getRoutine(SQLiteDatabase myDB, String routineName)
    {
        String query = "SELECT * FROM " + RoutineTable.RoutineEntry.TABLE_NAME + " WHERE " + RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {routineName});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            RoutineModel routineModel = new RoutineModel();

            while(c.moveToNext()){
                routineModel.setRoutineName(c.getString(c.getColumnIndexOrThrow(RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME)));
                routineModel.setRoutineDescription(c.getString(c.getColumnIndexOrThrow(RoutineTable.RoutineEntry.COLUMN_DESCRIPTION)));
            }

            return routineModel;
        }
    }

    public static List<RoutineModel> getAllRoutines(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + RoutineTable.RoutineEntry.TABLE_NAME;

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
                routineModel.setRoutineName(c.getString(c.getColumnIndexOrThrow(RoutineTable.RoutineEntry.COLUMN_ROUTINE_NAME)));
                routineModel.setRoutineDescription(c.getString(c.getColumnIndexOrThrow(RoutineTable.RoutineEntry.COLUMN_DESCRIPTION)));
                routines.add(routineModel);
            }

            return routines;
        }
    }
}
