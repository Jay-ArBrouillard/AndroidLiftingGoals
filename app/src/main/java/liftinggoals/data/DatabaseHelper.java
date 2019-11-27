package liftinggoals.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.calendar.Event;
import liftinggoals.models.ExerciseLogModel;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RecordModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.RoutineWorkoutModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "liftingGoals.db";
    public static final int DATABASE_VERSION = 65;

    public SQLiteDatabase myDB;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(RoutineTable.SQL_CREATE_ROUTINE_TABLE);
        db.execSQL(RoutineWorkoutsTable.SQL_CREATE_ROUTINE_WORKOUTS_TABLE);
        db.execSQL(WorkoutsTable.SQL_CREATE_WORKOUT_TABLE);
        db.execSQL(WorkoutExercisesTable.SQL_CREATE_WORKOUT_EXERCISES_TABLE);
        db.execSQL(ExercisesTable.SQL_CREATE_EXERCISE_TABLE);
        db.execSQL(ExerciseLogTable.SQL_CREATE_EXERCISE_LOG_TABLE);
        db.execSQL(RecordsTable.SQL_CREATE_RECORDS_TABLE);
        db.execSQL(MusclesTrainedTable.SQL_CREATE_MUSCLES_TRAINED_TABLE);
        db.execSQL(EventsTable.SQL_CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(RoutineTable.SQL_DROP_ROUTINE_TABLE);
        db.execSQL(RoutineWorkoutsTable.SQL_DROP_ROUTINE_WORKOUTS_TABLE);
        db.execSQL(WorkoutsTable.SQL_DROP_WORKOUT_TABLE);
        db.execSQL(WorkoutExercisesTable.SQL_DROP_WORKOUT_EXERCISES_TABLE);
        db.execSQL(ExercisesTable.SQL_DROP_EXERCISE_TABLE);
        db.execSQL(ExerciseLogTable.SQL_DROP_EXERCISE_LOG_TABLE);
        db.execSQL(RecordsTable.SQL_DROP_RECORDS_TABLE);
        db.execSQL(MusclesTrainedTable.SQL_DROP_MUSCLES_TRAINED_TABLE);
        db.execSQL(EventsTable.SQL_DROP_EVENTSTABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void openDB()
    {
        myDB = getWritableDatabase();
    }

    public void closeDB()
    {
        if (myDB != null && myDB.isOpen())
        {
            myDB.close();
        }
    }

    ////////////////////////////ROUTINE METHODS/////////////////////////////////////////////////////

    public long insertRoutine(int userId, String name, String description, int numberWorkouts)
    {
        return RoutineTable.insert(myDB, userId, name, description, numberWorkouts);
    }

    public long updateRoutineNameAndDescription(int routineId, String name, String description)
    {
        return RoutineTable.update(myDB, routineId, name, description);
    }

    public long updateRoutine(int routineId, String name, String description, int numberWorkouts)
    {
        return RoutineTable.update(myDB, routineId, name, description, numberWorkouts);
    }

    public long deleteRoutine(String name)
    {
        return RoutineTable.delete(myDB, name);
    }

    public RoutineModel getRoutine(String name)
    {
        return RoutineTable.getRoutine(myDB, name);
    }

    public List<RoutineModel> getAllRoutines()
    {
        return RoutineTable.getAllRoutines(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////ROUTINE WORKOUTS METHODS ///////////////////////////////////////////

    public long insertRoutineWorkout(int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.insert(myDB, routineId, workoutId);
    }

    public long updateRoutineWorkout(int routineWorkoutId ,int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.update(myDB, routineWorkoutId, routineId, workoutId);
    }

    public long deleteRoutineWorkout(int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.delete(myDB, routineId, workoutId);
    }

    public RoutineWorkoutModel getRoutineWorkout(int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.getRoutineWorkout(myDB, routineId, workoutId);
    }

    public List<RoutineWorkoutModel> getRoutineWorkoutsByRoutineId (int routineId)
    {
        return RoutineWorkoutsTable.getRoutineWorkoutsByRoutineId(myDB, routineId);
    }

    public List<RoutineWorkoutModel> getAllRoutineWorkouts()
    {
        return RoutineWorkoutsTable.getAllRoutineWorkouts(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////WORKOUT METHODS ////////////////////////////////////////////////////


    public long insertWorkout(String name, String description, double duration, int exercises)
    {
        return WorkoutsTable.insert(myDB, name, description, duration, exercises);
    }

    public long updateWorkout(int workoutId, String name, double duration)
    {
        return WorkoutsTable.update(myDB, workoutId, name, duration);
    }


    public long updateWorkout(int workoutId, String name, String description, double duration, int exercises)
    {
        return WorkoutsTable.update(myDB, workoutId, name, description, duration, exercises);
    }

    public long updateWorkoutName(int workoutId, String name)
    {
        return WorkoutsTable.update(myDB, workoutId, name);
    }


    public long deleteWorkout(int workoutId)
    {
        return WorkoutsTable.delete(myDB, workoutId);
    }

    public WorkoutModel getWorkout(int workoutId)
    {
        return WorkoutsTable.getWorkout(myDB, workoutId);
    }

    public List<WorkoutModel> getAllWorkouts()
    {
        return WorkoutsTable.getAllWorkouts(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////WORKOUT EXERCISE METHODS ///////////////////////////////////////////


    public long insertWorkoutExercise(int workoutId, int exerciseId, int minimumSets,
                                      int minimumReps, int maximumSets, int maximumReps, double intensity)
    {
        return WorkoutExercisesTable.insert(myDB, workoutId, exerciseId, minimumSets, minimumReps, maximumSets, maximumReps, intensity);
    }

    public long updateWorkoutExcercise(int workoutExerciseId, int minimumSets,
                              int minimumReps, int maximumSets, int maximumReps, double intensity)
    {
        return WorkoutExercisesTable.update(myDB, workoutExerciseId, minimumSets, minimumReps, maximumSets, maximumReps, intensity);
    }

    public long deleteWorkoutExercise(int workoutExerciseId)
    {
        return WorkoutExercisesTable.delete(myDB, workoutExerciseId);
    }

    public WorkoutExerciseModel getWorkoutExercise(int workoutExerciseId)
    {
        return WorkoutExercisesTable.getWorkoutExercise(myDB, workoutExerciseId);
    }

    public WorkoutExerciseModel getWorkoutExerciseByWorkoutAndExerciseId(int workoutId, int exerciseId)
    {
        return WorkoutExercisesTable.getWorkoutExercise(myDB, workoutId, exerciseId);
    }

    public List<WorkoutExerciseModel> getAllWorkoutExercisesByWorkoutId(int workoutId)
    {
        return WorkoutExercisesTable.getAllWorkoutExercisesByWorkoutId(myDB, workoutId);
    }

    public List<Integer> getAllWorkoutExerciseIdsByExerciseId(int exerciseId)
    {
        return WorkoutExercisesTable.getAllWorkoutExerciseIdsByExerciseId(myDB, exerciseId);
    }

    public List<WorkoutExerciseModel> getAllWorkoutExercises()
    {
        return WorkoutExercisesTable.getAllWorkoutExercises(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////EXERCISE METHODS ///////////////////////////////////////////////////

    public long insertExercise(String exerciseName)
    {
        return ExercisesTable.insert(myDB, exerciseName);
    }

    public long updateExerciseName(String exerciseName)
    {
        return ExercisesTable.update(myDB, exerciseName);
    }

    public long deleteExercise(String exerciseName)
    {
        return ExercisesTable.delete(myDB, exerciseName);
    }

    public ExerciseModel getExerciseByExerciseName(String exerciseName)
    {
        return ExercisesTable.getExerciseByExerciseName(myDB, exerciseName);
    }


    public ExerciseModel getExercise(int exerciseId)
    {
        return ExercisesTable.getExercise(myDB, exerciseId);
    }

    public List<ExerciseModel> getAllExercises()
    {
        return ExercisesTable.getAllExercises(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////RECORD METHODS /////////////////////////////////////////////////////


    public long insertRecord(int userId, int exercise_id, double intensity, int reps, String date)
    {
        return RecordsTable.insert(myDB, userId, exercise_id, intensity, reps, date);
    }

    public long updateRecord(int userId, int exercise_id, double intensity, int reps, String date)
    {
        return RecordsTable.update(myDB, userId, exercise_id, intensity, reps, date);
    }

    public long deleteRecord(int recordId)
    {
        return RecordsTable.delete(myDB, recordId);
    }

    public RecordModel getRecord(int recordId)
    {
        return RecordsTable.getRecord(myDB, recordId);
    }

    public List<RecordModel> getRecordsByExerciseId(int exerciseId)
    {
        return RecordsTable.getRecordsByExerciseId(myDB, exerciseId);
    }

    public List<RecordModel> getAllRecords()
    {
        return RecordsTable.getAllRecords(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////EXERCISE LOG METHODS ///////////////////////////////////////////////

    public long insertExerciseLog(ExerciseLogModel exerciseLogModel)
    {
        return ExerciseLogTable.insert(myDB, exerciseLogModel);
    }

    public List<ExerciseLogModel> getExercisesLogsByWorkoutExerciseId(int workoutExerciseId)
    {
        return ExerciseLogTable.getExercisesLogsByWorkoutExerciseId(myDB, workoutExerciseId);
    }

    public List<ExerciseLogModel> getExercisesLogsByExerciseId(int exerciseId)
    {
        List<ExerciseLogModel> results = new ArrayList<>();

        List<Integer> ids = getAllWorkoutExerciseIdsByExerciseId(exerciseId);

        for (Integer id : ids)
        {
            List<ExerciseLogModel> temp = getExercisesLogsByWorkoutExerciseId(id);

            if (temp != null && temp.size() > 0)
            {
                results.addAll(temp);
            }
        }

        return results;
    }

    public List<ExerciseLogModel> getAllExerciseLogs()
    {
        return ExerciseLogTable.getAllExercisesLogs(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////EVENT METHODS ///////////////////////////////////////////////

    public long insertEvent(String event, String time, String date, String month, String year)
    {
        return EventsTable.insert(myDB, event, time, date, month, year);
    }

    public List<Event> getEventsByMonthAndYear (String month, String year)
    {
        return EventsTable.getEventsForMonth(myDB, month, year);
    }

}
