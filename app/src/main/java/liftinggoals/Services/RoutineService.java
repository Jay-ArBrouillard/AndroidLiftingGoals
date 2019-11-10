package liftinggoals.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.RoutineWorkoutModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.classes.WorkoutModel;
import liftinggoals.data.DatabaseHelper;

public class RoutineService extends IntentService {
    private static final String TAG = "RoutineService";
    private DatabaseHelper db;
    private String databaseUpdated;

    public RoutineService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        fetchRemote();
    }

    private void fetchRemote() {
        db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        final ArrayList<RoutineModel> routineModels =  new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://3.221.56.60/initializeRoutines.php";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (Request.Method.GET, url, null, new
                Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        try
                        {
                            ArrayList<WorkoutModel> listOfWorkouts; //Ex: Workout A and Workout B
                            ArrayList<WorkoutExerciseModel> listOfExercises; //Ex: Workout A contains Squat(rep,set data), Bench, Row
                            String routineName = null;
                            String routineDesc = null;
                            int i = 0;
                            int length = response.length();
                            while (i < length)
                            {
                                listOfWorkouts = new ArrayList<>(); //Ex: Workout A and Workout B
                                listOfExercises = new ArrayList<>(); //Ex: Workout A contains Squat(rep,set data), Bench, Row
                                JSONObject routineObj = response.getJSONObject(i);
                                routineName = routineObj.getString("routine_name");
                                routineDesc = routineObj.getString("description");
                                int userId = routineObj.getInt("user_id");
                                int totalWorkouts = routineObj.getInt("number_workouts");

                                if (db.getRoutine(routineName) == null) {
                                    db.insertRoutine(userId, routineName, routineDesc, totalWorkouts);
                                    databaseUpdated = "added";
                                }
                                else {
                                    int routineId = Integer.parseInt(routineObj.getString("routine_id"));
                                    db.updateRoutine( routineId, routineName, routineDesc, totalWorkouts);
                                    databaseUpdated = "updated";
                                }

                                for (int j = 0 ; j < totalWorkouts; j++)
                                {
                                    if (i+1 == length)
                                    {
                                        break;
                                    }
                                    i++;
                                    JSONObject routineWorkoutObj = response.getJSONObject(i);
                                    int routineId = routineWorkoutObj.getInt("routine_id");
                                    int workoutId = routineWorkoutObj.getInt("workout_id");
                                    if (routineWorkoutObj.has("routine_workout_id") && db.getRoutineWorkout(routineId,workoutId) == null)
                                    {
                                        db.insertRoutineWorkout(routineId, workoutId);
                                    }
                                    else
                                    {
                                        int routineWorkoutId = Integer.parseInt(routineWorkoutObj.getString("routine_workout_id"));
                                        db.updateRoutineWorkout(routineWorkoutId, routineId, workoutId);
                                    }

                                    i++;
                                    JSONObject nextObj = response.getJSONObject(i);
                                    listOfWorkouts.add(new WorkoutModel(nextObj.getString("workout_name"), nextObj.getString("description"), nextObj.getDouble("duration")));
                                    int totalExercises = nextObj.getInt("number_exercises");

                                    if (db.getWorkout(nextObj.getInt("workout_id")) == null)
                                    {
                                        db.insertWorkout(nextObj.getString("workout_name"), nextObj.getString("description"), nextObj.getDouble("duration"), totalExercises);
                                    }
                                    else
                                    {
                                        db.updateWorkout(workoutId, nextObj.getString("workout_name"), nextObj.getString("description"), nextObj.getDouble("duration"), totalExercises);
                                    }


                                    listOfExercises.clear();
                                    WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();
                                    for (int k = 0; k < totalExercises*2; k++)
                                    {
                                        if (i+1 == length)
                                        {
                                            break;
                                        }
                                        i++;
                                        JSONObject exerciseObj = response.getJSONObject(i);
                                        if (k % 2 == 0)
                                        {
                                            String strMinSets = exerciseObj.getString("minimum_sets");
                                            String strMinReps = exerciseObj.getString("minimum_reps");
                                            String strMaxSets = exerciseObj.getString("maximum_sets");
                                            String strMaxReps = exerciseObj.getString("maximum_reps");
                                            //Quick null fix
                                            if (strMinSets == "null")
                                                strMinSets = "-1";
                                            if (strMinReps == "null")
                                                strMinReps = "-1";
                                            if (strMaxSets == "null")
                                                strMaxSets = "-1";
                                            if (strMaxReps == "null")
                                                strMaxReps = "-1";
                                            //Fix later
                                            workoutExerciseModel.setMinimumReps(Integer.parseInt(strMinSets));
                                            workoutExerciseModel.setMinimumReps(Integer.parseInt(strMinReps));
                                            workoutExerciseModel.setMaximumSets(Integer.parseInt(strMaxSets));
                                            workoutExerciseModel.setMaximumReps(Integer.parseInt(strMaxReps));

                                            if (db.getWorkoutExercise(exerciseObj.getInt("workout_exercise_id")) == null)
                                            {
                                                db.insertWorkoutExercise(exerciseObj.getInt("workout_id"), exerciseObj.getInt("exercise_id"),
                                                        Integer.parseInt(strMinSets), Integer.parseInt(strMinReps), Integer.parseInt(strMaxSets), Integer.parseInt(strMaxReps));
                                            }
                                            else
                                            {
                                                db.updateWorkoutExcercise(workoutId, exerciseObj.getInt("workout_id"), exerciseObj.getInt("exercise_id"),
                                                        Integer.parseInt(strMinSets), Integer.parseInt(strMinReps), Integer.parseInt(strMaxSets), Integer.parseInt(strMaxReps));
                                            }
                                        }
                                        else
                                        {
                                            String exerciseName = exerciseObj.getString("exercise_name");
                                            int exerciseId = exerciseObj.getInt("exercise_id");
                                            if (db.getExercise(exerciseId) == null)
                                            {
                                                db.insertExercise(exerciseName);
                                            }
                                            else {
                                                db.updateExerciseName(exerciseName);
                                            }
                                            //Create ExerciseModel for Workout
                                            ExerciseModel exerciseModel = new ExerciseModel(exerciseName);
                                            workoutExerciseModel.setExercise(exerciseModel);

                                            listOfExercises.add(workoutExerciseModel);
                                            workoutExerciseModel = new WorkoutExerciseModel(); //Clear exercise model
                                        }
                                    }

                                    listOfWorkouts.get(listOfWorkouts.size()-1).setExercises(listOfExercises);
                                }

                                routineModels.add(new RoutineModel(routineName, routineDesc, listOfWorkouts));

                                i++;
                            }   //End outer for loop

                            //String message = intent.getStringExtra("message");
                            //intent.setAction("action");
                            //SystemClock.sleep(3000);
                            //String echoMessage = "IntentService after a pause of 3 seconds echoes " + message;
                            if (databaseUpdated != null)
                            {
                                Intent intent = new Intent("action");
                                intent.putExtra("updatedRoutines", routineModels);

                                if (databaseUpdated.equals("added"))
                                {
                                    intent.putExtra("message", "New Default Routine added");
                                }
                                else if (databaseUpdated.equals("updated")) {
                                    intent.putExtra("message", "Default Routines were automatically updated");
                                }

                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                            }

                            db.closeDB();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsObjRequest);
    }

    private void printLocalDatabase()
    {
        for (RoutineModel r : db.getAllRoutines())
        {
            System.out.println(r.getRoutineName() + ": " + r.getRoutineDescription());
        }

        for (RoutineWorkoutModel rWM : db.getAllRoutineWorkouts())
        {
            System.out.println(rWM.getRoutineId() + ": " + rWM.getWorkoutId());
        }


        for (WorkoutModel w : db.getAllWorkouts())
        {
            System.out.println(w.getWorkoutName() + ": " + w.getDescription() + ": " + w.getEstimatedDuration() + ": " + w.getNumberExercises());
        }

        for (WorkoutExerciseModel wEM : db.getAllWorkoutExercises())
        {
            System.out.println(wEM.getWorkoutId() + ": " + wEM.getExerciseId() + ": " + wEM.getMinimumSets() + ": " + wEM.getMinimumReps() + ": " + wEM.getMaximumSets() + ": " + wEM.getMaximumSets());
        }

        for (ExerciseModel e : db.getAllExercises())
        {
            System.out.println(e.getExerciseName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
