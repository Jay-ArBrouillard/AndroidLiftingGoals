package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import liftinggoals.calendar.Event;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RecordModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.UserModel;
import liftinggoals.models.WorkoutModel;

public class SettingsActivity extends AppCompatActivity {
    private Button exportData;
    private DatabaseHelper db;
    private ArrayList<String> selected = new ArrayList<>();
    private static final String EVENT_CSV = "Events.csv";
    private static final String EXERCISE_CSV = "Exercises.csv";
    private static final String RECORDS_CSV = "Records.csv";
    private static final String ROUTINES_CSV = "Routines.csv";
    private static final String USERS_CSV = "Users.csv";
    private static final String WORKOUTS_CSV = "Workouts.csv";
    private Switch exportSwitch;
    private TextView subscriptionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (db == null)
        {
            db = new DatabaseHelper(this);
            db.openDB();
        }

        final ListView checkableList = findViewById(R.id.checkable_list);
        checkableList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final String [] items = {EVENT_CSV, EXERCISE_CSV, RECORDS_CSV, ROUTINES_CSV, USERS_CSV, WORKOUTS_CSV};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.checked_text_view_row_layout, R.id.txt_lan, items);
        checkableList.setAdapter(adapter);

        checkableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if (selected.contains(selectedItem))
                {
                    selected.remove(selectedItem);
                }
                else
                {
                    selected.add(selectedItem);
                }
            }
        });

        exportData = findViewById(R.id.settings_activity_export_data);
        exportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });


        exportSwitch = findViewById(R.id.settings_activity_switch);
        exportSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    ((TextView)findViewById(R.id.settings_activity_export_data)).setText("Export to 1 CSV");
                }
                else
                {
                    ((TextView)findViewById(R.id.settings_activity_export_data)).setText("Export to Separate CSVs");
                }
            }
        });

        subscriptionStatus = findViewById(R.id.settings_activity_sub_status);
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        int userId = sp.getInt("UserId", -1);
        if (userId == 1)
        {
            subscriptionStatus.setText("Admin");
        }
        else
        {
            subscriptionStatus.setText("Normal");
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_settings_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private void export()
    {
        if (selected.size() == 0)
        {
            Toast.makeText(getApplicationContext(), "No csv files selected for export", Toast.LENGTH_SHORT).show();
            return;
        }

        if (exportSwitch.isChecked())
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < selected.size(); i++)
            {
                String current = selected.get(i);
                StringBuilder builder = new StringBuilder();
                if (i != 0)
                {
                    stringBuilder.append("\n\n");
                }

                if (current.equals(EVENT_CSV))
                {
                    stringBuilder.append(convertEventsToString(builder));
                }
                else if (current.equals(EXERCISE_CSV))
                {
                    stringBuilder.append(convertExercisesToString(builder));
                }
                else if (current.equals(RECORDS_CSV))
                {
                    stringBuilder.append(convertRecordsToString(builder));
                }
                else if (current.equals(ROUTINES_CSV))
                {
                    stringBuilder.append(convertRoutinesToString(builder));
                }
                else if (current.equals(USERS_CSV))
                {
                    stringBuilder.append(convertUsersToString(builder));
                }
                else if (current.equals(WORKOUTS_CSV))
                {
                    stringBuilder.append(convertWorkoutsToString(builder));
                }
            }

            sendData("All data", stringBuilder.toString());
        }
        else
        {
            for (int i = 0; i < selected.size(); i++)
            {
                String current = selected.get(i);
                StringBuilder stringBuilder = new StringBuilder();
                String outputData = null;

                if (current.equals(EVENT_CSV))
                {
                    outputData = convertEventsToString(stringBuilder);
                }
                else if (current.equals(EXERCISE_CSV))
                {
                    outputData = convertExercisesToString(stringBuilder);
                }
                else if (current.equals(RECORDS_CSV))
                {
                    outputData = convertRecordsToString(stringBuilder);
                }
                else if (current.equals(ROUTINES_CSV))
                {
                    outputData = convertRoutinesToString(stringBuilder);
                }
                else if (current.equals(USERS_CSV))
                {
                    outputData = convertUsersToString(stringBuilder);
                }
                else if (current.equals(WORKOUTS_CSV))
                {
                    outputData = convertWorkoutsToString(stringBuilder);
                }

                sendData(current, outputData);
            }
        }

    }

    private String convertWorkoutsToString(StringBuilder stringBuilder)
    {
        List<WorkoutModel> workoutModels = db.getAllWorkouts();
        stringBuilder.append("WorkoutId, Workout Name, Description, Duration, Number of Exercises");
        for (WorkoutModel w : workoutModels)
        {
            w.setDescription(w.getDescription().replace(",", " "));
            stringBuilder.append("\n").append(w.getWorkoutId()).append(",").append(w.getWorkoutName()).append(",").append(w.getDescription()).append(",").append(w.getEstimatedDuration()).append(",").append(w.getNumberExercises());
        }

        return stringBuilder.toString();
    }

    private String convertUsersToString(StringBuilder stringBuilder)
    {
        List<UserModel> userModels = db.getAllUsers();
        stringBuilder.append("UserId, Username, Password, getIsAdmin, Last Login Time");
        for (UserModel r : userModels)
        {
            stringBuilder.append("\n").append(r.getUserId()).append(",").append(r.getUsername()).append(",").append(r.getPassword()).append(",").append(r.getIsAdmin()).append(",").append(r.getLastLogin());
        }

        return stringBuilder.toString();
    }

    private String convertRoutinesToString(StringBuilder stringBuilder)
    {
        List<RoutineModel> routineModels = db.getAllRoutines();
        stringBuilder.append("routineId, UserId, routine Name, Description, Default routine");
        for (RoutineModel r : routineModels)
        {
            stringBuilder.append("\n").append(r.getRoutineId()).append(",").append(r.getUserId()).append(",").append(r.getRoutineName()).append(",").append(r.getRoutineDescription()).append(",").append(r.getDefaultRoutine());
        }

        return  stringBuilder.toString();
    }

    private String convertRecordsToString(StringBuilder stringBuilder)
    {
        List<RecordModel> recordModels = db.getAllRecords();
        stringBuilder.append("UserId, ExerciseId, Intensity, Reps Performed, Date");
        for (RecordModel r : recordModels)
        {
            stringBuilder.append("\n").append(r.getUserId()).append(",").append(r.getExerciseId()).append(",").append(r.getIntensity()).append(",").append(r.getRepsPerformed()).append(",").append(r.getDate());
        }

        return stringBuilder.toString();
    }

    private String convertExercisesToString(StringBuilder stringBuilder)
    {
        List<ExerciseModel> exerciseModels = db.getAllExercises();
        stringBuilder.append("Name, ExerciseId, UserId");
        for (ExerciseModel e : exerciseModels)
        {
            stringBuilder.append("\n").append(e.getExerciseName()).append(",").append(e.getExerciseId()).append(",").append(e.getUserId());
        }

        return stringBuilder.toString();
    }

    private String convertEventsToString(StringBuilder stringBuilder)
    {
        List<Event> events = db.getAllEvents();
        stringBuilder.append("User Id, Event, Time, Date, Month, Year, Extended Date, Exercises");
        for (Event e : events)
        {
            e.setFULL_DATE(e.getFULL_DATE().replace(",","")); //Remove commas
            e.setExercises(e.getExercises().replace(","," ")); //Remove commas replace with space
            stringBuilder.append("\n").append(e.getUserId()).append(",").append(e.getEVENT());
            stringBuilder.append(",").append(e.getTIME()).append(",").append(e.getDATE()).append(",").append(e.getMONTH());
            stringBuilder.append(",").append(e.getYEAR()).append(",").append(e.getFULL_DATE());
            stringBuilder.append(",").append(e.getExercises());
        }

        return stringBuilder.toString();
    }

    private void sendData(String csvName, String data)
    {
        try {
            FileOutputStream out = openFileOutput(csvName, Context.MODE_PRIVATE);
            out.write(data.getBytes());

            Context context  = getApplicationContext();
            File fileLocation = new File(getFilesDir(), csvName);
            Uri path = FileProvider.getUriForFile(context, "lifting_goals", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, csvName);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(fileIntent.createChooser(fileIntent, "Send CSV data"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(SettingsActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(SettingsActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(SettingsActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(SettingsActivity.this, MapsActivity.class);
                    break;
            }
            if (selectedActivity != null)
            {
                startActivity(selectedActivity);
            }

            return true;    //Means we want to select the clicked item
        }
    };

    @Override
    protected void onDestroy() {
        db.closeDB();
        super.onDestroy();
    }
}