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
import liftinggoals.models.WorkoutModel;

public class WorkoutService extends IntentService {
    private static final String TAG = "WorkoutService";
    private RequestQueue queue;
    private WorkoutModel workout;
    private int routineId;

    public WorkoutService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
        {
            queue = Volley.newRequestQueue(getApplicationContext());
            workout = new WorkoutModel();
            workout.setWorkoutId(intent.getIntExtra("workoutId", -1));
            routineId = intent.getIntExtra("routineId", -1);

            String type = intent.getStringExtra("type");
            if (type.equals("insert"))
            {
                insertWorkout();
            }
            else if (type.equals("update"))
            {
                updateWorkout();
            }
            else if (type.equals("delete"))
            {
                deleteWorkout();
            }
        }
    }

    private void updateWorkout()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = "http://3.221.56.60/updateWorkout.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error updating workout", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.updateWorkout(workout.getWorkoutId(), workout.getWorkoutName(), workout.getDescription(), workout.getEstimatedDuration(), workout.getNumberExercises());
                    Toast.makeText(getApplicationContext(), "Successfully updated workout", Toast.LENGTH_LONG).show();
                }

                db.closeDB();

                Intent intent = new Intent("workoutAction");
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
                Intent intent = new Intent("workoutAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error updating Working", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("workoutId", Integer.toString(workout.getWorkoutId()));
                params.put("workoutName", workout.getWorkoutName());
                params.put("workoutDesc", workout.getDescription());
                params.put("duration", Double.toString(workout.getEstimatedDuration()));
                params.put("numExercises", Integer.toString(workout.getNumberExercises()));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void insertWorkout()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = String.format("http://3.221.56.60/insertWorkout.php?routineId=%s", routineId);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error adding workout", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String [] ids = response.split(",");
                    int workoutId = Integer.parseInt(ids[0].trim());
                    int routineWorkoutId = Integer.parseInt(ids[1].trim());
                    db.insertWorkout(workoutId, "Untitled Workout", "Untitled Description", 0.0, 0);
                    db.insertRoutineWorkout(routineWorkoutId, routineId, workoutId);
                    Toast.makeText(getApplicationContext(), "Successfully added workout", Toast.LENGTH_LONG).show();
                }
                db.closeDB();

                Intent intent = new Intent("workoutAction");
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
                Intent intent = new Intent("workoutAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error saving Workout", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void deleteWorkout()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = String.format("http://3.221.56.60/deleteWorkout.php?workoutId=%s", Integer.toString(workout.getWorkoutId()));
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error deleting workout", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.deleteWorkout(workout.getWorkoutId());
                    db.deleteRoutineWorkout(routineId, workout.getWorkoutId());     //TODO this should be primary key
                    Toast.makeText(getApplicationContext(), "Successfully deleted workout", Toast.LENGTH_LONG).show();
                }
                db.closeDB();

                Intent intent = new Intent("workoutAction");
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

                    } else if (error.getClass().equals(TimeoutError.class))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Connectivity error. Try again",
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
                Intent intent = new Intent("workoutAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error deleting Workout", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
