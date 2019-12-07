package liftinggoals.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.calendar.Event;
import liftinggoals.models.ExerciseLogModel;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.MusclesTrainedModel;
import liftinggoals.models.RecordModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.RoutineWorkoutModel;
import liftinggoals.models.UserModel;
import liftinggoals.models.UserRoutineModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "liftingGoals.db";
    public static final int DATABASE_VERSION = 235;

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
        db.execSQL(UserTable.SQL_CREATE_USER_TABLE);
        db.execSQL(UserRoutinesTable.SQL_CREATE_USER_ROUTINE_TABLE);
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
        db.execSQL(EventsTable.SQL_DROP_EVENTS_TABLE);
        db.execSQL(UserTable.SQL_DROP_USER_TABLE);
        db.execSQL(UserRoutinesTable.SQL_DROP_USER_ROUTINE_TABLE);
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

    public long insertRoutine(int pk, int userId, String name, String description, int numberWorkouts, int defaultRoutine)
    {
        return RoutineTable.insert(myDB, pk, userId, name, description, numberWorkouts, defaultRoutine);
    }

    public long insertRoutine(int userId, String name, String description, int numberWorkouts, int defaultRoutine)
    {
        return RoutineTable.insert(myDB, userId, name, description, numberWorkouts, defaultRoutine);
    }

    public long updateRoutineNameAndDescription(int routineId, String name, String description)
    {
        return RoutineTable.update(myDB, routineId, name, description);
    }

    public long updateRoutine(int routineId, String name, String description, int numberWorkouts)
    {
        return RoutineTable.update(myDB, routineId, name, description, numberWorkouts);
    }

    public long deleteRoutine(int routineId)
    {
        return RoutineTable.delete(myDB, routineId);
    }

    public RoutineModel getRoutine(int routineId)
    {
        return RoutineTable.getRoutine(myDB, routineId);
    }

    public List<RoutineModel> getAllRoutines()
    {
        return RoutineTable.getAll(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////ROUTINE WORKOUTS METHODS ///////////////////////////////////////////

    public long insertRoutineWorkout(int pk, int routineId, int workoutId)
    {
        return RoutineWorkoutsTable.insert(myDB, pk, routineId, workoutId);
    }

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

    public RoutineWorkoutModel getRoutineWorkout(int routineWorkoutId)
    {
        return RoutineWorkoutsTable.get(myDB, routineWorkoutId);
    }

    public List<RoutineWorkoutModel> getRoutineWorkoutsByRoutineId (int routineId)
    {
        return RoutineWorkoutsTable.getRoutineWorkoutsByRoutineId(myDB, routineId);
    }

    public List<RoutineModel> getAllRoutinesForUser(int userId)
    {
        List<RoutineModel> allRoutines = RoutineTable.getAll(myDB);
        List<UserRoutineModel> allUserRoutines = UserRoutinesTable.getAll(myDB);

        if (allRoutines == null) {
            return null;
        }

        if (allUserRoutines == null)
        {
            return null;
        }

        List<RoutineModel> results = new ArrayList<>();
        for (UserRoutineModel user : allUserRoutines)
        {
            int routineId = user.getRoutineId();
            if (user.getUserId() == userId)
            {
                RoutineModel toRemove = null;
                for (RoutineModel routine : allRoutines)
                {
                    if (routine.getRoutineId() == routineId)
                    {
                        results.add(routine);
                        toRemove = routine;
                        break;
                    }
                }
                allRoutines.remove(toRemove);
            }
        }

        return results;
    }

    public List<RoutineWorkoutModel> getAllRoutineWorkouts()
    {
        return RoutineWorkoutsTable.getAllRoutineWorkouts(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////WORKOUT METHODS ////////////////////////////////////////////////////

    public long insertWorkout(int pk, String name, String description, double duration, int exercises)
    {
        return WorkoutsTable.insert(myDB, pk, name, description, duration, exercises);
    }

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

    public long updateWorkoutNumExercises(int workoutId, int exercises)
    {
        return WorkoutsTable.update(myDB, workoutId, exercises);
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

    public long insertWorkoutExercise(int pk, int workoutId, int exerciseId, int minimumSets,
                                      int minimumReps, int maximumSets, int maximumReps, double intensity)
    {
        return WorkoutExercisesTable.insert(myDB, pk, workoutId, exerciseId, minimumSets, minimumReps, maximumSets, maximumReps, intensity);
    }

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

    public long insertExercise(int pk, String exerciseName, int userId)
    {
        return ExercisesTable.insert(myDB, pk, exerciseName, userId);
    }

    public long updateExerciseName(int pk, String exerciseName)
    {
        return ExercisesTable.update(myDB, pk, exerciseName);
    }

    public long deleteExercise(String exerciseName)
    {
        return ExercisesTable.delete(myDB, exerciseName);
    }

    public ExerciseModel getExerciseByExerciseNameAndUserId(String exerciseName, int userId)
    {
        return ExercisesTable.getExerciseByExerciseNameAndUserId(myDB, exerciseName, userId);
    }


    public ExerciseModel getExercise(int exerciseId)
    {
        return ExercisesTable.getExercise(myDB, exerciseId);
    }

    public List<ExerciseModel> getAllExercisesForUser(int userId)
    {
        List<ExerciseModel> allExercises = ExercisesTable.getAllExercises(myDB);
        if (allExercises == null) { return new ArrayList<>(); }

        ArrayList<ExerciseModel> results = new ArrayList<>();
        for (ExerciseModel e : allExercises)
        {
            if (e.getUserId() == userId)
            {
                results.add(e);
            }
        }
        return results;
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

    public long insertRecord(int recordId, RecordModel entity)
    {
        return RecordsTable.insert(myDB, recordId, entity);
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

    public List<RecordModel> getRecordsByExerciseIdForUser(int userId, int exerciseId )
    {
        List<RecordModel> allRecords = getAllRecords();
        ArrayList<RecordModel> results = new ArrayList<>();
        if (allRecords == null) { return null; }

        for (int i = 0; i < allRecords.size(); i++)
        {
            RecordModel curr = allRecords.get(i);
            if (curr.getUserId() == userId && curr.getExerciseId() == exerciseId)
            {
                results.add(curr);
            }
        }

        return results;
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

    public long insertExerciseLog(int userExerciseLogId, ExerciseLogModel exerciseLogModel)
    {
        return ExerciseLogTable.insert(myDB, userExerciseLogId, exerciseLogModel);
    }

    public long deleteExerciseLog(int userExerciseLogId)
    {
        return ExerciseLogTable.delete(myDB, userExerciseLogId);
    }

    public List<ExerciseLogModel> getExercisesLogsByWorkoutExerciseId(int workoutExerciseId)
    {
        return ExerciseLogTable.getExercisesLogsByWorkoutExerciseId(myDB, workoutExerciseId);
    }

    public List<ExerciseLogModel> getExercisesLogsByRoutineAndExercise(int userRoutineId, int workoutExerciseId)
    {
        return  ExerciseLogTable.getExercisesLogsByRoutineAndExercise(myDB, userRoutineId, workoutExerciseId);
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

    public List<ExerciseLogModel> getExercisesLogsByMuscleGroup(String muscleGroup)
    {
        List<ExerciseLogModel> results = new ArrayList<>();

        List<MusclesTrainedModel> musclesList = MusclesTrainedTable.get(myDB, muscleGroup);
        if (musclesList == null)
        {
            return results;
        }
        else
        {
            for (MusclesTrainedModel muscle : musclesList) {
                results.addAll(getExercisesLogsByExerciseId(muscle.getExerciseId()));
            }
        }

        return results;
    }

    public List<ExerciseLogModel> getAllExerciseLogs()
    {
        return ExerciseLogTable.getAllExercisesLogs(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////EVENT METHODS ///////////////////////////////////////////////

    public long insertEvent(int userId, String event, String time, String date, String month, String year, String longDate)
    {
        return EventsTable.insert(myDB, userId, event, time, date, month, year, longDate);
    }

    public long insertEventWithExercises(int eventId, int userId, String event, String exerciseInfo, String time, String date, String month, String year, String longDate)
    {
        return EventsTable.insert(myDB, eventId, userId, event, exerciseInfo, time, date, month, year, longDate);
    }

    public List<Event> getEventsByMonthAndYear(int userId, String month, String year)
    {
        return EventsTable.getEventsForMonth(myDB, userId, month, year);
    }

    public List<Event> getAllEvents()
    {
        return EventsTable.getAll(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////USER METHODS ///////////////////////////////////////////////

    public long insertUser(int userId, String username, String password, int isAdmin, String lastLogin)
    {
        return UserTable.insert(myDB, username, password, isAdmin, lastLogin);
    }

    public UserModel getUser(String username, String password)
    {
        return UserTable.get(myDB, username, password);
    }

    public UserModel getUser(int userId)
    {
        return UserTable.get(myDB, userId);
    }

    public long updateUserLoginTime(String username, String loginTime)
    {
        return UserTable.update(myDB, username, loginTime);
    }

    public List<UserModel> getAllUsers()
    {
        return UserTable.getAll(myDB);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////USER ROUTINE METHODS ////////////////////////////////////////

    public long insertUserRoutine(int pk, int userId, int routineId)
    {
        return UserRoutinesTable.insert(myDB, pk, userId, routineId);
    }

    public long insertUserRoutine(int userId, int routineId)
    {
        return UserRoutinesTable.insert(myDB, userId, routineId);
    }

    public UserRoutineModel getUserRoutine(int userRoutineId)
    {
        return UserRoutinesTable.get(myDB, userRoutineId);
    }

    public UserRoutineModel getUserRoutine(int userId, int routineId)
    {
        return UserRoutinesTable.get(myDB, userId, routineId);
    }

    public long updateUserRoutine(int userRoutineId, int userId, int routineId)
    {
        return UserRoutinesTable.update(myDB, userRoutineId, userId, routineId);
    }

    public List<UserRoutineModel> getAllUserRoutines()
    {
        return UserRoutinesTable.getAll(myDB);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Muscles METHODS ////////////////////////////////////////

    public long insertMuscleGroup(int pk, int exerciseId, String muscleGroup)
    {
        return MusclesTrainedTable.insert(myDB, pk, exerciseId, muscleGroup);
    }

    public long insertMuscleGroup(int exerciseId, String muscleGroup)
    {
        return MusclesTrainedTable.insert(myDB, exerciseId, muscleGroup);
    }

    public MusclesTrainedModel getMuscleGroup(int musclesTrainedId)
    {
        return MusclesTrainedTable.get(myDB, musclesTrainedId);
    }

    public List<MusclesTrainedModel> getMuscleGroupsByMuscleGroup(String muscleGroup)
    {
        return MusclesTrainedTable.get(myDB, muscleGroup);
    }

    public long updateMuscleGroup(int musclesTrainedId, int exerciseId, String muscleGroup)
    {
        return MusclesTrainedTable.update(myDB, musclesTrainedId, exerciseId, muscleGroup);
    }
}
