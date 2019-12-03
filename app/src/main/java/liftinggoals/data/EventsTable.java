package liftinggoals.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.calendar.Event;

public class EventsTable {

    public static final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " +
            EventEntry.TABLE_NAME + " (" +
            EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EventEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
            EventEntry.COLUMN_EVENT + " TEXT NOT NULL, " +
            EventEntry.COLUMN_EXERCISES + " TEXT, " +
            EventEntry.COLUMN_TIME + " TEXT, " +
            EventEntry.COLUMN_DATE + " TEXT, " +
            EventEntry.COLUMN_FULL_DATE + " TEXT, " +
            EventEntry.COLUMN_MONTH + " TEXT, " +
            EventEntry.COLUMN_YEAR + " TEXT" +
            ");";
    public static final String SQL_DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME;

    public static abstract class EventEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Events";
        public static final String COLUMN_EVENT = "event";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EXERCISES = "exercises";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_FULL_DATE = "full_date";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_YEAR = "year";
    }

    public static long insert(SQLiteDatabase myDB, int id, int userId, String event, String exerciseInfo, String time, String date, String month, String year, String longDate)
    {
        ContentValues values = new ContentValues();
        values.put(EventEntry._ID, id);
        values.put(EventEntry.COLUMN_USER_ID, userId);
        values.put(EventEntry.COLUMN_EVENT, event);
        values.put(EventEntry.COLUMN_EXERCISES, exerciseInfo);
        values.put(EventEntry.COLUMN_TIME, time);
        values.put(EventEntry.COLUMN_DATE, date);
        values.put(EventEntry.COLUMN_MONTH, month);
        values.put(EventEntry.COLUMN_YEAR, year);
        values.put(EventEntry.COLUMN_FULL_DATE, longDate);

        return myDB.insert(EventEntry.TABLE_NAME, null, values);
    }

    public static long insert(SQLiteDatabase myDB, int userId, String event, String time, String date, String month, String year, String longDate)
    {
        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_USER_ID, userId);
        values.put(EventEntry.COLUMN_EVENT, event);
        values.put(EventEntry.COLUMN_TIME, time);
        values.put(EventEntry.COLUMN_DATE, date);
        values.put(EventEntry.COLUMN_MONTH, month);
        values.put(EventEntry.COLUMN_YEAR, year);
        values.put(EventEntry.COLUMN_FULL_DATE, longDate);

        return myDB.insert(EventEntry.TABLE_NAME, null, values);
    }

    public static long update(SQLiteDatabase myDB, String name)
    {
        ContentValues values = new ContentValues();
        //values.put(ExerciseLogEntry.COLUMN_EXERCISE_NAME, name);

        return myDB.update(EventEntry.TABLE_NAME, values, "exercise_name = ?", new String[] {name});
    }

    public static long delete(SQLiteDatabase myDB, String name)
    {
        //String where = ExerciseLogEntry.COLUMN_EXERCISE_NAME + " = " + name;
        String where = null;
        return myDB.delete(EventEntry.TABLE_NAME, where, null);
    }

    public static Event getEvent(SQLiteDatabase myDB, String date)
    {
        String query = "SELECT * FROM " + EventEntry.TABLE_NAME + " WHERE " + EventEntry.COLUMN_DATE + " = ?";

        Cursor c = myDB.rawQuery(query, new String[] {date});

        if (c.getCount() == 0)
        {
            return null;
        }
        else
        {
            Event event = new Event();

            while(c.moveToNext()){
                event.setEVENT(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_EVENT)));
                event.setExercises(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_EXERCISES)));
                event.setMONTH(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_MONTH)));
                event.setTIME(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_TIME)));
                event.setDATE(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_DATE)));
                event.setYEAR(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_YEAR)));
                event.setFULL_DATE(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_FULL_DATE)));
            }

            return event;
        }
    }

    public static List<Event> getEventsForMonth(SQLiteDatabase myDB, int userId, String month, String year) {
        String query = "SELECT * FROM " + EventEntry.TABLE_NAME + " WHERE " + EventEntry.COLUMN_USER_ID + " = ? AND "
                                                                            + EventEntry.COLUMN_MONTH + " = ? AND "
                                                                            + EventEntry.COLUMN_YEAR + " = ?";

        Cursor c = myDB.rawQuery(query, new String[]{Integer.toString(userId), month, year});

        if (c.getCount() == 0) {
            return null;
        } else {
            ArrayList<Event> events = new ArrayList<>();

            while (c.moveToNext()) {
                Event event = new Event();
                event.setEVENT(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_EVENT)));
                event.setExercises(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_EXERCISES)));
                event.setMONTH(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_MONTH)));
                event.setTIME(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_TIME)));
                event.setDATE(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_DATE)));
                event.setYEAR(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_YEAR)));
                event.setFULL_DATE(c.getString(c.getColumnIndexOrThrow(EventEntry.COLUMN_FULL_DATE)));
                events.add(event);
            }
            return events;
        }
    }
}