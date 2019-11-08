package liftinggoals.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;

import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RecordModel;
import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.RoutineWorkoutModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.classes.WorkoutModel;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "liftingGoals.db";
    public static final int DATABASE_VERSION = 16;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(RoutineTable.SQL_DROP_ROUTINE_TABLE);
        db.execSQL(RoutineWorkoutsTable.SQL_DROP_ROUTINE_WORKOUTS_TABLE);
        db.execSQL(WorkoutsTable.SQL_DROP_WORKOUT_TABLE);
        db.execSQL(WorkoutExercisesTable.SQL_DROP_WORKOUT_EXERCISES_TABLE);
        db.execSQL(ExercisesTable.SQL_DROP_EXERCISE_TABLE);
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

    public long insertRoutine(String name, String description, int numberWorkouts)
    {
        return RoutineTable.insert(myDB, name, description, numberWorkouts);
    }

    public long updateRoutineName(String name)
    {
        return RoutineTable.update(myDB, name);
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

    public List<WorkoutExerciseModel> getAllWorkoutExercisesByWorkoutId(int workoutId)
    {
        return WorkoutExercisesTable.getAllWorkoutExercisesByWorkoutId(myDB, workoutId);
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


    public long insertRecord(int userId, int exercise_id, double intensity, int reps)
    {
        return RecordsTable.insert(myDB, userId, exercise_id, intensity, reps);
    }

    public long updateRecord(int userId, int exercise_id, double intensity, int reps)
    {
        return RecordsTable.update(myDB, userId, exercise_id, intensity, reps);
    }

    public long deleteRecord(int recordId)
    {
        return RecordsTable.delete(myDB, recordId);
    }

    public RecordModel getRecord(int recordId)
    {
        return RecordsTable.getRecord(myDB, recordId);
    }

    public List<RecordModel> getAllRecords()
    {
        return RecordsTable.getAllRecords(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}
