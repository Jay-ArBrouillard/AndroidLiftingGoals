package liftinggoals.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.User;

public class RoutineTable{

    public static final String SQL_CREATE_ROUTINE_TABLE = "CREATE TABLE " +
            RoutineEntry.TABLE_NAME + " (" +
            RoutineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RoutineEntry.COLUMN_ROUTINE_ID + " TEXT NOT NULL, " +
            RoutineEntry.COLUMN_ROUTINE_NAME + " TEXT NOT NULL, " +
            RoutineEntry.COLUMN_DESCRIPTION + " TEXT " +
            ");";
    public static final String SQL_DROP_ROUTINE_TABLE = "DROP TABLE IF EXISTS " + RoutineEntry.TABLE_NAME;


    public static abstract class RoutineEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Routines";
        public static final String COLUMN_ROUTINE_ID = "routine_id";
        public static final String COLUMN_ROUTINE_NAME = "routine_name";
        public static final String COLUMN_DESCRIPTION = "description";
    }
    public static long insert(SQLiteDatabase myDB, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);

        return myDB.insert(RoutineEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, String name, String description)
    {
        ContentValues values = new ContentValues();
        values.put(RoutineEntry.COLUMN_ROUTINE_NAME, name);
        values.put(RoutineEntry.COLUMN_DESCRIPTION, description);

        return myDB.update(RoutineEntry.TABLE_NAME, values, null, null);
    }

    public static long delete(SQLiteDatabase myDB, String name)
    {
        String where = RoutineEntry.COLUMN_ROUTINE_NAME + " = " + name;

        return myDB.delete(RoutineEntry.TABLE_NAME, where, null);
    }

    public static RoutineModel getRoutine(SQLiteDatabase myDB, String routineName)
    {
        String query = "SELECT * FROM " + RoutineEntry.TABLE_NAME + " WHERE " + RoutineEntry.COLUMN_ROUTINE_NAME  + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {routineName});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            RoutineModel routineModel = new RoutineModel();

            while(c.moveToNext()){
                routineModel.setRoutineName(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_ROUTINE_NAME)));
                routineModel.setRoutineDescription(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_DESCRIPTION)));
            }

            return routineModel;
        }
    }

    public static List<RoutineModel> getAllRoutines(SQLiteDatabase myDB)
    {
        String query = "SELECT * FROM " + RoutineEntry.TABLE_NAME;

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
                routineModel.setRoutineName(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_ROUTINE_NAME)));
                routineModel.setRoutineDescription(c.getString(c.getColumnIndexOrThrow(RoutineEntry.COLUMN_DESCRIPTION)));
                routines.add(routineModel);
            }

            return routines;
        }
    }


}
