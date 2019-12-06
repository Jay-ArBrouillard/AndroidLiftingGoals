package liftinggoals.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import liftinggoals.calendar.Event;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.models.ExerciseLogModel;
import liftinggoals.models.RecordModel;

public class LogEventRecordService extends IntentService {
    private static final String TAG = "LogEventRecordService";
    private RequestQueue queue;
    private ArrayList<ExerciseLogModel> loggedExercises;
    private Event event;
    private int userId;
    private ArrayList<RecordModel> loggedRecords;
    private boolean sentErrorMsg = false;

    public LogEventRecordService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
        {
            queue = Volley.newRequestQueue(getApplicationContext());
            loggedExercises = intent.getParcelableArrayListExtra("loggedExercises");
            event = intent.getParcelableExtra("eventObject");
            userId = intent.getIntExtra("userId", -1);
            loggedRecords = intent.getParcelableArrayListExtra("loggedRecords");

            String type = intent.getStringExtra("type");
            if (type.contains("exerciseLog"))
            {
                insertExerciseLog();
            }

            if (type.contains("event"))
            {
                insertEvent();
            }

            if (type.contains("record"))
            {
                insertUpdateRecord();
            }
        }
    }

    private void insertUpdateRecord()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        if (loggedRecords.size() == 0)
        {
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "No records to log", Toast.LENGTH_LONG).show();
                }
            });

            Intent intent = new Intent("recordsAction");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            db.closeDB();
            return;
        }

        final String recordsString = new Gson().toJson(loggedRecords);

        String url = "http://3.221.56.60/insertUpdateRecord.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error updating records", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String trimmedReponse = response.trim();
                    String ids [] = trimmedReponse.split(" ");
                    if (trimmedReponse.isEmpty() || ids.length == 0)
                    {
                        Toast.makeText(getApplicationContext(), "No records to log", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Delete local database records but they won't match the primary keys in remote
                        for (int i = 0; i < loggedRecords.size(); i++)
                        {
                            RecordModel curr = loggedRecords.get(i);
                            db.deleteRecord(curr.getRecordId());
                        }

                        //Reenter records logs into local with primary keys from remote
                        for (int i = 0; i < ids.length; i++)
                        {
                            RecordModel curr = loggedRecords.get(i);
                            if (db.getRecord(Integer.parseInt(ids[i])) == null)
                            {
                                db.insertRecord(Integer.parseInt(ids[i]), curr);
                            }
                            else
                            {
                                long result = db.updateRecord(curr.getUserId(), curr.getExerciseId(), curr.getIntensity(), curr.getRepsPerformed(), curr.getDate());
                                System.out.println("result: " + result);
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Successfully added records", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent("recordsAction");
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

                if (!sentErrorMsg)
                {
                    Intent intent = new Intent("errorRecordsAction");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Toast.makeText(getApplicationContext(), "Error saving records", Toast.LENGTH_LONG).show();
                }
                sentErrorMsg = true;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recordsList", recordsString);
                return params;
            }
        };

        queue.add(stringRequest);

    }


    private void insertExerciseLog()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        final String exerciseLogsString = new Gson().toJson(loggedExercises);

        String url = "http://3.221.56.60/insertExerciseLogs.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error adding exercise log", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String trimmedReponse = response.trim();
                    String ids [] = trimmedReponse.split(" ");
                    if (trimmedReponse.isEmpty() || ids.length == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Completed. No exercises to log", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Delete local database exerciselogs but they won't match the primary keys in remote
                        for (int i = 0; i < loggedExercises.size(); i++)
                        {
                            ExerciseLogModel curr = loggedExercises.get(i);
                            db.deleteExerciseLog(curr.getUserExerciseLogId());
                        }

                        //Reenter exercise logs into local with primary keys from remote
                        for (int i = 0; i < ids.length; i++)
                        {
                            ExerciseLogModel curr = loggedExercises.get(i);
                            db.insertExerciseLog(Integer.parseInt(ids[i]), curr);
                        }
                        Toast.makeText(getApplicationContext(), "Successfully added exercise log", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent("exerciseLogAction");
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

                if (!sentErrorMsg)
                {
                    Intent intent = new Intent("errorExerciseLogAction");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Toast.makeText(getApplicationContext(), "Error saving exercise log", Toast.LENGTH_LONG).show();
                }
                sentErrorMsg = true;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("exerciseLogsList", exerciseLogsString);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void insertEvent()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        String url = "http://3.221.56.60/insertEvent.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("-1"))
                {
                    Toast.makeText(getApplicationContext(), "Error adding event", Toast.LENGTH_LONG).show();
                }
                else
                {
                    int eventId = Integer.parseInt(response.trim());
                    db.insertEventWithExercises(eventId, userId, event.getEVENT(), event.getExercises(), event.getTIME(), event.getDATE(), event.getMONTH(), event.getYEAR(), event.getFULL_DATE());
                    Toast.makeText(getApplicationContext(), "Successfully added event", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("insertEventAction");
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
                if (!sentErrorMsg)
                {
                    Intent intent = new Intent("errorEventAction");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Toast.makeText(getApplicationContext(), "Error saving event", Toast.LENGTH_LONG).show();
                }
                sentErrorMsg = true;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event", event.getEVENT());
                params.put("userId", Integer.toString(userId));
                params.put("time", event.getTIME());
                params.put("date", event.getDATE());
                params.put("month", event.getMONTH());
                params.put("year", event.getYEAR());
                params.put("fullDate", event.getFULL_DATE());
                params.put("exercises", event.getExercises());
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

