package liftinggoals.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
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
import liftinggoals.models.RoutineModel;

public class RoutineService extends IntentService {
    private static final String TAG = "RoutineService";
    private RequestQueue queue;
    private RoutineModel routine;

    public RoutineService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
        {
            queue = Volley.newRequestQueue(getApplicationContext());
            routine = new RoutineModel();
            routine.setUserId(intent.getIntExtra("userId", -1));
            routine.setRoutineId(intent.getIntExtra("routineId", -1));
            routine.setRoutineName(intent.getStringExtra("routineName"));
            routine.setRoutineDescription(intent.getStringExtra("description"));

            String type = intent.getStringExtra("type");
            if (type.equals("insert"))
            {
                insertRoutine();
            }
            else if (type.equals("update"))
            {
                updateRoutine();
            }
            else if (type.equals("delete"))
            {
                deleteRoutine();
            }
        }
    }

    private void updateRoutine()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = "http://3.221.56.60/updateRoutine.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error updating routine", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.updateRoutineNameAndDescription(routine.getRoutineId(), routine.getRoutineName(), routine.getRoutineDescription());
                    Toast.makeText(getApplicationContext(), "Successfully updated routine", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent("routineAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
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
                Intent intent = new Intent("routineAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error updating Routine", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("routineId", Integer.toString(routine.getRoutineId()));
                params.put("routineName", routine.getRoutineName());
                params.put("routineDesc", routine.getRoutineDescription());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void insertRoutine()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = String.format("http://3.221.56.60/insertRoutine.php?userId=%s", routine.getUserId());
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error adding routine", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String [] ids = response.split(",");
                    int routineId = Integer.parseInt(ids[0].trim());
                    int userRoutineId = Integer.parseInt(ids[1].trim());
                    int temp = routine.getUserId();
                    db.insertRoutine(routineId, routine.getUserId(), "Untitled Routine", "Untitled Description", 0, 0);
                    db.insertUserRoutine(userRoutineId, temp, routineId);
                    Toast.makeText(getApplicationContext(), "Successfully added routine", Toast.LENGTH_LONG).show();
                }
                db.closeDB();

                Intent intent = new Intent("routineAction");
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
                Intent intent = new Intent("routineAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error saving Routine", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void deleteRoutine()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = String.format("http://3.221.56.60/deleteRoutine.php?routineId=%s", Integer.toString(routine.getRoutineId()));
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error deleting routine", Toast.LENGTH_LONG).show();
                }
                else
                {
                    db.deleteRoutine(routine.getRoutineId());
                    Toast.makeText(getApplicationContext(), "Successfully deleted routine", Toast.LENGTH_LONG).show();
                }
                db.closeDB();
                Intent intent = new Intent("routineAction");
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
                Intent intent = new Intent("routineAction");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "Error deleting Routine", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
