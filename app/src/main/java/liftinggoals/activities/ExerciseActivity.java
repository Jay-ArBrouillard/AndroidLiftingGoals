package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.liftinggoals.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import liftinggoals.calendar.Event;
import liftinggoals.models.ExerciseLogModel;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RecordModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.services.LogEventRecordService;

public class ExerciseActivity extends AppCompatActivity {
    private LineChart lineChart;
    private Button doneButton;
    private Button logButton;
    private ArrayList<RoutineModel> routineModels;
    private ArrayList<WorkoutExerciseModel> workoutExerciseModels;
    private ArrayList<ExerciseLogModel> loggedExercises;
    private ArrayList<RecordModel> loggedRecords;
    private int selectedRoutineIndex;
    private int selectedWorkoutIndex;
    private DatabaseHelper db;
    private ArrayList<Entry> entries = new ArrayList<>();
    private Spinner spinner;
    private TextView weightTextView;
    private TextView repsTextView;
    private TextView setsTextView;
    private TextView countDownTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftMillis = 6000; //1 min
    private boolean timerRunning;
    private String workoutName;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.ENGLISH);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private SimpleDateFormat longDateFormat = new SimpleDateFormat("MMMM, dd, yyyy, hh:mm a", Locale.ENGLISH);
    private int userId;
    private ResponseReceiver myReceiver;
    private boolean exerciseLogSaved = false;
    private boolean eventSaved = false;
    private boolean recordSaved = false;
    private LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        selectedRoutineIndex = sp.getInt("selected_routine_index", -1);
        selectedWorkoutIndex = sp.getInt("selected_workout_index", -1);
        userId = sp.getInt("UserId", -1);
        routineModels = getIntent().getExtras().getParcelableArrayList("routine_models");
        workoutExerciseModels = getIntent().getExtras().getParcelableArrayList("exercises");
        workoutName = getIntent().getExtras().getString("workout_name");
        loggedExercises = new ArrayList<>();
        loggedRecords = new ArrayList<>();

        if (db == null)
        {
            db = new DatabaseHelper(this);
            db.openDB();
        }

        loadingAnim = findViewById(R.id.exercise_activity_loading_animation);
        spinner = findViewById(R.id.exercise_activity_spinner);
        doneButton = findViewById(R.id.activity_exercise_done_button);
        logButton = findViewById(R.id.exercise_activity_log_button);
        weightTextView = findViewById(R.id.exercise_activity_weight_value);
        repsTextView = findViewById(R.id.exercise_activity_reps_value);
        setsTextView = findViewById(R.id.exercise_activity_set_value);
        countDownTextView = findViewById(R.id.exercise_activity_timer);

        loadingAnim.cancelAnimation();
        loadingAnim.setVisibility(View.INVISIBLE);
        countDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        if (workoutExerciseModels.size() > 0)
        {
            logButton.setEnabled(true);
        }
        else
        {
            logButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Oh no you can't log any exercises until you add some on the Workouts Page", Toast.LENGTH_LONG).show();
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String time = dateFormat.format(calendar.getTime());
                final String date = eventDateFormat.format(calendar.getTime());
                final String month = monthFormat.format(calendar.getTime());
                final String year = yearFormat.format(calendar.getTime());
                String longDate = longDateFormat.format(calendar.getTime());
                String exerciseDescription = buildExerciseString(); //Exercise A, Exercise B, Exercise C

                Event event = new Event();
                event.setEVENT(workoutName);
                event.setExercises(exerciseDescription);
                event.setYEAR(year);
                event.setDATE(date);
                event.setTIME(time);
                event.setMONTH(month);
                event.setFULL_DATE(longDate);

                Intent insertAllExerciseLogAndEvent = new Intent(ExerciseActivity.this, LogEventRecordService.class);
                insertAllExerciseLogAndEvent.putExtra("userId", userId);
                insertAllExerciseLogAndEvent.putExtra("loggedExercises", loggedExercises);
                insertAllExerciseLogAndEvent.putExtra("eventObject", event);
                insertAllExerciseLogAndEvent.putExtra("loggedRecords", loggedRecords);
                String insertString = "";
                if (!exerciseLogSaved) { insertString +="exerciseLog"; }
                if (!eventSaved) { insertString += "event"; }
                if (!recordSaved) { insertString += "record"; }
                insertAllExerciseLogAndEvent.putExtra("type", insertString);
                startService(insertAllExerciseLogAndEvent);
                if (!loadingAnim.isAnimating())
                {
                    loadingAnim.setVisibility(View.VISIBLE);
                    loadingAnim.playAnimation();
                }

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //Quit the animation if it takes longer than 15 seconds
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingAnim.isAnimating())
                                {
                                    loadingAnim.cancelAnimation();
                                    loadingAnim.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                        timer.cancel();
                    }
                }, 15000);
            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logButton.setEnabled(false);    //Prevent double button click

                ExerciseLogModel exerciseLogModel = new ExerciseLogModel();
                exerciseLogModel.setRpe(0.0);
                exerciseLogModel.setRestDuration(0.0);
                exerciseLogModel.setTempo("0");
                TextView set = findViewById(R.id.exercise_activity_set_value);
                EditText repsEditText = findViewById(R.id.exercise_activity_reps_value);
                EditText weightEditText = findViewById(R.id.exercise_activity_weight_value);

                try {
                    int selectedIndex = spinner.getSelectedItemPosition();
                    int index = Integer.parseInt(set.getText().toString());
                    int reps = Integer.parseInt(repsEditText.getText().toString().trim());
                    double weight = Double.parseDouble(weightEditText.getText().toString().trim());
                    int exerciseId = workoutExerciseModels.get(selectedIndex).getExerciseId();

                    exerciseLogModel.setWorkoutExerciseId(workoutExerciseModels.get(selectedIndex).getWorkoutExerciseId());
                    exerciseLogModel.setUserRoutineId(db.getUserRoutine(userId, routineModels.get(selectedRoutineIndex).getRoutineId()).getUserRoutineId()); //TODO fix
                    exerciseLogModel.setSetPerformed(index);
                    exerciseLogModel.setRepsPerformed(reps);
                    exerciseLogModel.setIntensity(weight);
                    exerciseLogModel.setDate(longDateFormat.format(calendar.getTime()));

                    index++;
                    set.setText(Integer.toString(index));
                    long exerciseLogId = db.insertExerciseLog(exerciseLogModel);
                    exerciseLogModel.setUserExerciseLogId((int) exerciseLogId);
                    loggedExercises.add(exerciseLogModel);
                    long recordId = processRecords(userId, exerciseId, weight, reps, longDateFormat.format(calendar.getTime()));
                    if (recordId != -1)
                    {
                        loggedRecords.add(new RecordModel((int)recordId, userId, exerciseId, weight, reps, longDateFormat.format(calendar.getTime())));
                    }
                    buildLineGraph(workoutExerciseModels.get(selectedIndex)); //Do this last
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(), "Type in a valid value for weight and reps", Toast.LENGTH_LONG).show();
                }
                logButton.setEnabled(true);    //Prevent double button click
            }
        });

        ArrayList<String> exerciseNames = new ArrayList<>();
        for (WorkoutExerciseModel e : workoutExerciseModels)
        {
            exerciseNames.add(processString(e, new StringBuilder()));
        }
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_list_item_1, exerciseNames);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(exerciseAdapter);

        spinner.setSelection(-1, true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WorkoutExerciseModel selected = workoutExerciseModels.get(position);
                buildLineGraph(selected);

                double intensity = selected.getIntensity();
                if (intensity != 0 && intensity != -1)
                {
                    weightTextView.setText(Double.toString(intensity));
                }

                int minimumReps = selected.getMinimumReps();
                if (minimumReps != 0 && minimumReps != -1)
                {
                    repsTextView.setText(Integer.toString(minimumReps));
                }

                int lastSetForExercise = 0; //Set to zero in the case that no set was found
                for (ExerciseLogModel logged : loggedExercises)
                {
                    if (logged.getWorkoutExerciseId() == selected.getWorkoutExerciseId())
                    {
                        lastSetForExercise = logged.getSetPerformed();
                    }
                }
                lastSetForExercise++; //Whatever the last set we found was we want the next set after it
                setsTextView.setText(""+lastSetForExercise);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

                //this leaves the keyboard hidden on load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void startStop()
    {
        if (timerRunning)
        {
            stopTimer();
        }
        else
        {
            startTimer();
        }
    }

    private void startTimer()
    {
        countDownTimer =  new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                int minutes = (int) timeLeftMillis / 6000;
                int seconds = (int) timeLeftMillis % 6000 / 1000;
                String timeLeftText= "" + minutes + ":";
                if (seconds < 10)
                {
                    timeLeftText += "0";
                }
                timeLeftText += seconds;
                countDownTextView.setText(timeLeftText);
            }

            @Override
            public void onFinish() {
                countDownTextView.setText("0:00");
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(1000);
            }
        }.start();

        timerRunning = true;
    }

    private void stopTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;
    }

    private String buildExerciseString()
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < workoutExerciseModels.size(); i++)
        {
            ExerciseModel exercise = workoutExerciseModels.get(i).getExercise();
            builder.append(exercise.getExerciseName());

            if (i != (workoutExerciseModels.size() - 1))
            {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    private int processRecords(int userId, int exerciseId, double weight, int reps, String date)
    {
        ArrayList<RecordModel> existing = (ArrayList<RecordModel>) db.getAllRecords();

        if (existing == null)
        {
            //First Record
            Toast.makeText(getApplicationContext(), "You logged your first weight ever: " + weight + " LBS for " + reps + " reps", Toast.LENGTH_LONG).show();
            long recordId = db.insertRecord(userId, exerciseId,  weight, reps, date);
            return (int) recordId;
        }

        boolean foundRep = false;
        for (RecordModel record : existing)
        {
            if (record.getRepsPerformed() == reps)
            {
                foundRep = true;
            }

            if (record.getRepsPerformed() == reps && record.getIntensity() < weight)
            {
                //New Record
                Toast.makeText(getApplicationContext(), "Congrats on the PR! " + weight + " LBS for " + reps + " reps", Toast.LENGTH_LONG).show();
                long recordId = db.updateRecord(userId, exerciseId,  weight, reps, date);
                return (int) recordId;
            }
        }

        if (!foundRep)
        {
            //New Record
            Toast.makeText(getApplicationContext(), "You logged your first weight: " + weight + " LBS for " + reps + " reps", Toast.LENGTH_LONG).show();
            long recordId = db.insertRecord(userId, exerciseId,  weight, reps, date);
            return (int) recordId;
        }

        return -1;
    }

    private String processString(WorkoutExerciseModel e, StringBuilder stringBuilder)
    {
        ExerciseModel exerciseModel = e.getExercise();
        int minSets = e.getMinimumSets();
        int maxSets = e.getMaximumSets();
        int minReps = e.getMinimumReps();
        int maxReps = e.getMaximumReps();

        if (minSets == -1 && maxSets == -1 && minReps == -1 && maxReps == -1)
        {
            return exerciseModel.getExerciseName();
        }
        else if (minSets == -1 && maxSets == -1)
        {
            stringBuilder.append(exerciseModel.getExerciseName()).append(" (");

            if (minReps == -1 && maxReps != -1)
            {
                stringBuilder.append(maxReps).append(" Reps");
            }
            else if (minReps != -1 && maxSets == -1)
            {
                stringBuilder.append(minReps).append("+").append(" Reps");
            }
            else if (minReps != -1 && maxReps != -1)
            {
                stringBuilder.append(minReps).append("-").append(maxReps).append(" Reps");
            }

            stringBuilder.append(")");
        }
        else if (minReps == -1 && maxReps == -1)
        {
            stringBuilder.append(exerciseModel.getExerciseName()).append(" (");

            if (minSets == -1 && maxSets != -1)
            {
                stringBuilder.append(maxSets).append(" Sets");
            }
            else if (minSets != -1 && maxSets == -1)
            {
                stringBuilder.append(minSets).append("+").append(" Sets");
            }
            else if (minSets != -1 && maxSets != -1)
            {
                stringBuilder.append(minSets).append("-").append(maxSets).append(" Sets");
            }

            stringBuilder.append(")");
        }
        else
        {
            stringBuilder.append(exerciseModel.getExerciseName()).append(" (");

            if (minSets == -1 && maxSets != -1)
            {
                stringBuilder.append(maxSets).append(" Sets");
            }
            else if (minSets != -1 && maxSets == -1)
            {
                stringBuilder.append(minSets).append("+").append(" Sets");
            }
            else if (minSets != -1 && maxSets != -1)
            {
                stringBuilder.append(minSets).append("-").append(maxSets).append(" Sets");
            }

            stringBuilder.append(", ");

            if (minReps == -1 && maxReps != -1)
            {
                stringBuilder.append(maxReps).append(" Reps");
            }
            else if (minReps != -1 && maxSets == -1)
            {
                stringBuilder.append(minReps).append("+").append(" Reps");
            }
            else if (minReps != -1 && maxReps != -1)
            {
                stringBuilder.append(minReps).append("-").append(maxReps).append(" Reps");
            }

            stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }

    private boolean setData(WorkoutExerciseModel workoutExerciseModel)
    {
        ArrayList<ExerciseLogModel> exerciseLogModelArrayList = new ArrayList<>();
        for (ExerciseLogModel e : loggedExercises)
        {
            if (workoutExerciseModel.getWorkoutExerciseId() == e.getWorkoutExerciseId())
            {
                exerciseLogModelArrayList.add(e);
            }
        }

        if (exerciseLogModelArrayList.size() == 0)
        {
            return false;
        }

        entries.clear();

        for (ExerciseLogModel logModel : exerciseLogModelArrayList)
        {
            String reps = Integer.toString(logModel.getRepsPerformed());
            String intensity = Double.toString(logModel.getIntensity());

            entries.add(new Entry(Float.valueOf(reps), Float.valueOf(intensity)));
        }

        Collections.sort(entries, new EntryXComparator());
        return true;
    }

    private void buildLineGraph(WorkoutExerciseModel workoutExerciseModel)
    {
        if (!setData(workoutExerciseModel))
        {
            entries = new ArrayList<>();
        }

        lineChart = findViewById(R.id.line_graph);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lineChart.setMinimumHeight(displayMetrics.heightPixels/2);

        lineChart.setScaleEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setDrawMarkers(false);

        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.enableDashedLine(10,8,15);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);

        lineChart.setVisibility(View.VISIBLE);
        lineChart.animateY(100);
        lineChart.setData(data);
        lineChart.invalidate();

        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(true);

        lineChart.setDrawBorders(true);
        lineChart.setPinchZoom(false);

        //Axes settings
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisRight = lineChart.getAxisRight();
        YAxis yAxisLeft = lineChart.getAxisLeft();

        xAxis.setGranularity(1f);
        yAxisLeft.setGranularity(5f);
        yAxisRight.setGranularity(5f);

        Description description = new Description();
        description.setText("Log an exercise to see data");
        lineChart.setDescription(description);
    }

    private void setReceiver() {
        myReceiver = new ResponseReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("exerciseLogAction");
        intentFilter.addAction("insertEventAction");
        intentFilter.addAction("recordsAction");
        intentFilter.addAction("errorExerciseLogAction");
        intentFilter.addAction("errorRecordsAction");
        intentFilter.addAction("errorEventAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("exerciseLogAction"))
            {
                exerciseLogSaved = true;
                entries.clear();
                loggedExercises.clear();
            }
            else if (intent.getAction().equals("insertEventAction"))
            {
                eventSaved = true;
            }
            else if (intent.getAction().equals("recordsAction"))
            {
                recordSaved = true;
                loggedRecords.clear();
            }

            if (exerciseLogSaved && eventSaved && recordSaved)
            {
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                Intent finishWorkoutIntent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                startActivity(finishWorkoutIntent);
                finish();
            }

            if (intent.getAction().equals("errorExerciseLogAction"))
            {
                if (loadingAnim.isAnimating())
                {
                    loadingAnim.cancelAnimation();
                    loadingAnim.setVisibility(View.INVISIBLE);
                }
            }
            else if (intent.getAction().equals("errorRecordsAction"))
            {
                if (loadingAnim.isAnimating())
                {
                    loadingAnim.cancelAnimation();
                    loadingAnim.setVisibility(View.INVISIBLE);
                }
            }
            else if(intent.getAction().equals("errorEventAction"))
            {
                if (loadingAnim.isAnimating())
                {
                    loadingAnim.cancelAnimation();
                    loadingAnim.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        setReceiver();
        super.onStart();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onStop();
    }

}