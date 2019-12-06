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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import liftinggoals.data.DatabaseHelper;

public class ExerciseMuscleGroupService extends IntentService {
    private static final String TAG = "ExerciseMuscleGroupService";
    private RequestQueue queue;
    private String exerciseName;
    private String muscleGroupsString; //ex: Chest Arms Abs
    private ArrayList<String> muscleGroups;
    private int userId;

    public ExerciseMuscleGroupService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
        {
            queue = Volley.newRequestQueue(getApplicationContext());
            exerciseName = intent.getStringExtra("exerciseName");
            muscleGroupsString = intent.getStringExtra("muscleGroupsString");
            muscleGroups = intent.getStringArrayListExtra("muscleGroupsList");
            userId = intent.getIntExtra("userId", -1);

            String type = intent.getStringExtra("type");
            if (type.equals("insert"))
            {
                insertExerciseAndMuscleGroup();
            }
            else if (type.equals("update"))
            {
                updateWorkout();
            }
        }
    }

    private void updateWorkout()
    {
    }

    private void insertExerciseAndMuscleGroup()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = "http://3.221.56.60/insertExerciseMuscleGroup.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error adding exercise", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String stringReponse = response.trim();
                    String [] ids = stringReponse.split(" ");
                    int exerciseId = Integer.parseInt(ids[0]);
                    db.insertExercise(exerciseId, exerciseName, userId);
                    for (int i = 1; i < ids.length; i++)
                    {
                        db.insertMuscleGroup(Integer.parseInt(ids[i]), exerciseId, muscleGroups.get(i-1));
                    }
                    Toast.makeText(getApplicationContext(), "Successfully added exercise", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("exerciseAction");
                    intent.putExtra("insertedExerciseIdPk", exerciseId);
                    intent.putExtra("exerciseName", exerciseName);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                db.closeDB();


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
                Intent intent = new Intent("exerciseAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error saving exercise", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("exerciseName", exerciseName);
                params.put("userId", Integer.toString(userId));
                params.put("muscleGroupsList", muscleGroupsString);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

