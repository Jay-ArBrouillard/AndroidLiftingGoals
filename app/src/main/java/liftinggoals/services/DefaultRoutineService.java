package liftinggoals.services;

import android.app.IntentService;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.RoutineWorkoutModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;
import liftinggoals.data.DatabaseHelper;

public class DefaultRoutineService extends IntentService {
    private static final String TAG = "DefaultRoutineService";
    private DatabaseHelper db;

    public DefaultRoutineService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
        {
            fetchRemote();
        }
    }

    private void fetchRemote() {
        db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        //TODO add muscle groups to musclesTrainedTable

        final ArrayList<RoutineModel> routineModels =  new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://3.221.56.60/fetchdefaultRoutines.php";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (Request.Method.GET, url, null, new
                Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        try
                        {
                            int length = response.length();
                            for (int i = 0; i < length; i++)
                            {
                                JSONObject json = response.getJSONObject(i);
                                if (json.has("user_routine_id"))
                                {
                                    int userRoutineId = json.getInt("user_routine_id");
                                    int userId = json.getInt("user_id");
                                    int routineId = json.getInt("routine_id");

                                    if (db.getUserRoutine(userRoutineId) == null)
                                    {
                                        db.insertUserRoutine(userRoutineId, userId, routineId);
                                    }
                                    else
                                    {
                                        db.updateUserRoutine(userRoutineId, userId, routineId);
                                    }
                                }
                                else if (json.has("routine_name"))
                                {
                                    String routineName = Html.fromHtml(json.getString("routine_name")).toString();
                                    String routineDesc = Html.fromHtml(json.getString("description")).toString();
                                    int routineId = json.getInt("routine_id");
                                    int userId = json.getInt("user_id");
                                    int totalWorkouts = json.getInt("number_workouts");

                                    if (db.getRoutine(routineId) == null)
                                    {
                                        db.insertRoutine(routineId, userId, routineName, routineDesc, totalWorkouts);
                                    }
                                    else
                                    {
                                        db.updateRoutine(routineId, routineName, routineDesc, totalWorkouts);
                                    }
                                }
                                else if (json.has("routine_workout_id"))
                                {
                                    int routineWorkoutId = json.getInt("routine_workout_id");
                                    int routineId = json.getInt("routine_id");
                                    int workoutId = json.getInt("workout_id");

                                    if (db.getRoutineWorkout(routineWorkoutId) == null)
                                    {
                                        db.insertRoutineWorkout(routineWorkoutId , routineId, workoutId);
                                    }
                                    else
                                    {
                                        db.updateRoutineWorkout(routineWorkoutId, routineId, workoutId);
                                    }
                                }
                                else if (json.has("workout_name"))
                                {
                                    int workoutId = json.getInt("workout_id");
                                    String workoutName = Html.fromHtml(json.getString("workout_name")).toString();
                                    String workoutDesc = Html.fromHtml(json.getString("description")).toString();
                                    double duration = json.getDouble("duration");
                                    int totalExercises = json.getInt("number_exercises");

                                    if (db.getWorkout(workoutId) == null)
                                    {
                                        db.insertWorkout(workoutId, workoutName, workoutDesc, duration, totalExercises);
                                    }
                                    else
                                    {
                                        db.updateWorkout(workoutId, workoutName, workoutDesc, duration, totalExercises);
                                    }
                                }
                                else if (json.has("workout_exercise_id"))
                                {
                                    int workoutExerciseId = json.getInt("workout_exercise_id");
                                    int workoutId = json.getInt("workout_id");
                                    int exerciseId = json.getInt("exercise_id");
                                    String strMinSets = json.getString("minimum_sets");
                                    String strMinReps = json.getString("minimum_reps");
                                    String strMaxSets = json.getString("maximum_sets");
                                    String strMaxReps = json.getString("maximum_reps");
                                    String strIntensity = json.getString("intensity");
                                    //Quick null fix
                                    if (strMinSets == "null")
                                        strMinSets = "-1";
                                    if (strMinReps == "null")
                                        strMinReps = "-1";
                                    if (strMaxSets == "null")
                                        strMaxSets = "-1";
                                    if (strMaxReps == "null")
                                        strMaxReps = "-1";
                                    if (strIntensity == "null")
                                        strIntensity = "-1";
                                    //Fix later
                                    if (db.getWorkoutExercise(workoutExerciseId) == null)
                                    {
                                        db.insertWorkoutExercise(workoutExerciseId, workoutId, exerciseId,
                                                Integer.parseInt(strMinSets), Integer.parseInt(strMinReps), Integer.parseInt(strMaxSets), Integer.parseInt(strMaxReps), Double.parseDouble(strIntensity));
                                    }
                                    else
                                    {
                                        db.updateWorkoutExcercise(workoutExerciseId, Integer.parseInt(strMinSets), Integer.parseInt(strMinReps),
                                                Integer.parseInt(strMaxSets), Integer.parseInt(strMaxReps), Double.parseDouble(strIntensity));
                                    }

                                }
                                else if (json.has("exercise_id") && json.has("exercise_name"))
                                {
                                    int exerciseId = json.getInt("exercise_id");
                                    String exerciseName = json.getString("exercise_name");

                                    if (db.getExercise(exerciseId) == null)
                                    {
                                        db.insertExercise(exerciseId, exerciseName);
                                    }
                                    else
                                    {
                                        db.updateExerciseName(exerciseId, exerciseName);
                                    }
                                }
                                else if (json.has("muscles_trained_id"))
                                {
                                    int musclesTrainedId = json.getInt("muscles_trained_id");
                                    int exerciseId = json.getInt("exercise_id");
                                    String muscleGroup = json.getString("muscle_group");

                                    if (db.getMuscleGroup(musclesTrainedId) == null)
                                    {
                                        db.insertMuscleGroup(musclesTrainedId, exerciseId, muscleGroup);
                                    }
                                    else
                                    {
                                        db.updateMuscleGroup(musclesTrainedId, exerciseId, muscleGroup);
                                    }
                                }
                            }

                            Intent intent = new Intent("action");
                            intent.putExtra("message", "Default routines added");

                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                            db.closeDB();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(NoConnectionError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "No internet connection",
                                Toast.LENGTH_LONG).show();

                    } else if (error.getClass().equals(TimeoutError.class)) {
                        Toast.makeText(getApplicationContext(),
                                "Connectivity error. Try checking your internet and/or wifi",
                                Toast.LENGTH_LONG).show();
                    }
                }
                error.printStackTrace();
            }
        });

        queue.add(jsObjRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
