package liftinggoals.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import liftinggoals.data.DatabaseHelper;
import liftinggoals.models.WorkoutExerciseModel;

public class WorkoutExerciseService extends IntentService {
    private static final String TAG = "WorkoutExerciseService";
    private RequestQueue queue;
    private WorkoutExerciseModel workoutExerciseModel;
    private int numExercises;

    public WorkoutExerciseService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
        {
            queue = Volley.newRequestQueue(getApplicationContext());
            workoutExerciseModel = intent.getParcelableExtra("workoutExerciseModel");
            if (workoutExerciseModel == null) { workoutExerciseModel = new WorkoutExerciseModel(); }
            if (intent.hasExtra("workoutId"))
            {
                workoutExerciseModel.setWorkoutId(intent.getIntExtra("workoutId", -1));
            }
            if (intent.hasExtra("exerciseId"))
            {
                workoutExerciseModel.setExerciseId(intent.getIntExtra("exerciseId", -1));
            }
            numExercises = intent.getIntExtra("numExercises", -1);

            String type = intent.getStringExtra("type");
            if (type.equals("insert"))
            {
                insertWorkoutExercise();
            }
            else if (type.equals("update"))
            {
                updateWorkoutExercise();
            }
        }
    }

    private void updateWorkoutExercise()
    {
       final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = "http://3.221.56.60/updateWorkoutExercise.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error updating workout exercise", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.updateWorkoutExcercise(workoutExerciseModel.getWorkoutExerciseId(), workoutExerciseModel.getMinimumSets(),
                                                                 workoutExerciseModel.getMinimumReps(),
                                                                 workoutExerciseModel.getMaximumSets(),
                                                                 workoutExerciseModel.getMaximumReps(),
                                                                 workoutExerciseModel.getIntensity());
                    Toast.makeText(getApplicationContext(), "Successfully updated workout exercise", Toast.LENGTH_LONG).show();
                }

                db.closeDB();

                Intent intent = new Intent("workoutEditAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(NoConnectionError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "No internet connection",
                                Toast.LENGTH_LONG).show();

                    } else if (error.getClass().equals(TimeoutError.class)) {
                        Toast.makeText(getApplicationContext(),
                                "Connectivity error. Try checking your internet and/or wifi then try again",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (error.getClass().equals(VolleyError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Volley Error",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (error.getClass().equals(AuthFailureError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Authentication Error",
                                Toast.LENGTH_LONG).show();
                    }
                }
                error.printStackTrace();
                db.closeDB();
                Intent intent = new Intent("workoutEditAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error updating Working", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("minSets", Integer.toString(workoutExerciseModel.getMinimumSets()));
                params.put("minReps", Integer.toString(workoutExerciseModel.getMinimumReps()));
                params.put("maxSets", Integer.toString(workoutExerciseModel.getMaximumSets()));
                params.put("maxReps", Integer.toString(workoutExerciseModel.getMaximumReps()));
                params.put("intensity", Double.toString(workoutExerciseModel.getIntensity()));
                params.put("workoutExerciseId", Integer.toString(workoutExerciseModel.getWorkoutExerciseId()));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void insertWorkoutExercise()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = String.format("http://3.221.56.60/insertWorkoutExercise.php?workoutId=%s&exerciseId=%s", Integer.toString(workoutExerciseModel.getWorkoutId()), Integer.toString(workoutExerciseModel.getExerciseId()));
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error adding workout exercise", Toast.LENGTH_LONG).show();
                }
                else
                {
                    int workoutExerciseId = Integer.parseInt(response.trim());
                    db.insertWorkoutExercise(workoutExerciseId, workoutExerciseModel.getWorkoutId(), workoutExerciseModel.getExerciseId(), 0,0,0,0,0);
                    db.updateWorkoutNumExercises(workoutExerciseModel.getWorkoutId(), numExercises);
                    Toast.makeText(getApplicationContext(), "Successfully added workout exercise", Toast.LENGTH_LONG).show();
                }
                db.closeDB();

                Intent intent = new Intent("workoutEditAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(NoConnectionError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "No internet connection",
                                Toast.LENGTH_LONG).show();

                    }
                    else if (error.getClass().equals(TimeoutError.class)) {
                        Toast.makeText(getApplicationContext(),
                                "Connectivity error. Try checking your internet and/or wifi then try again",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (error.getClass().equals(VolleyError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Volley Error",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (error.getClass().equals(AuthFailureError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Authentication Error",
                                Toast.LENGTH_LONG).show();
                    }
                }
                error.printStackTrace();
                db.closeDB();
                Intent intent = new Intent("workoutEditAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error saving workout exercise", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
