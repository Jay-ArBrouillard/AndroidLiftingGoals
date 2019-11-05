package liftinggoals.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.RoutineWorkoutModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.classes.WorkoutModel;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "liftingGoals.db";
    public static final int DATABASE_VERSION = 11;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(RoutineTable.SQL_DROP_ROUTINE_TABLE);
        db.execSQL(RoutineWorkoutsTable.SQL_DROP_ROUTINE_WORKOUTS_TABLE);
        db.execSQL(WorkoutsTable.SQL_DROP_WORKOUT_TABLE);
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

    public long insertRoutine(String name, String description)
    {
        return RoutineTable.insert(myDB, name, description);
    }

    public long updateRoutine(String name, String description)
    {
        return RoutineTable.update(myDB, name, description);
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

    public long updateRoutineWorkout(int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.update(myDB, routineId, workoutId);
    }

    public long deleteRoutineWorkout(int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.delete(myDB, routineId, workoutId);
    }

    public RoutineWorkoutModel getRoutineWorkout(int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.getRoutineWorkout(myDB, routineId, workoutId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////WORKOUT METHODS ////////////////////////////////////////////////////


    public long insertWorkout(String name, String description, double duration, int exercises)
    {
        return WorkoutsTable.insert(myDB, name, description, duration, exercises);
    }

    public long updateWorkout(String name, String description, double duration, int exercises)
    {
        return WorkoutsTable.update(myDB, name, description, duration, exercises);
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
                                      int minimumReps, int maximumSets, int maximumReps)
    {
        return WorkoutExercisesTable.insert(myDB, workoutId, exerciseId, minimumSets, minimumReps, maximumSets, maximumReps);
    }

    public long updateWorkoutExcercise(int workoutId, int exerciseId, int minimumSets,
                              int minimumReps, int maximumSets, int maximumReps)
    {
        return WorkoutExercisesTable.update(myDB, workoutId, exerciseId, minimumSets, minimumReps, maximumSets, maximumReps);
    }

    public long deleteWorkoutExercise(int workoutExerciseId)
    {
        return WorkoutExercisesTable.delete(myDB, workoutExerciseId);
    }

    public WorkoutExerciseModel getWorkoutExercise(int workoutExerciseId)
    {
        return WorkoutExercisesTable.getWorkoutExercise(myDB, workoutExerciseId);
    }

    public List<WorkoutExerciseModel> getAllWorkoutExercises()
    {
        return WorkoutExercisesTable.getAllWorkoutExercises(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}
