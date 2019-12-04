package liftinggoals.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import liftinggoals.adapters.WorkoutEditAdapter;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.dialogs.CreateExerciseDialog;
import liftinggoals.misc.RoutineModelHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.dialogs.WorkoutEditDialog;
import liftinggoals.models.WorkoutModel;
import liftinggoals.services.ExerciseMuscleGroupService;
import liftinggoals.services.WorkoutExerciseService;
import liftinggoals.services.WorkoutService;

public class WorkoutEditActivity extends AppCompatActivity implements WorkoutEditDialog.WorkoutEditDialogListener, CreateExerciseDialog.CreateExerciseDialogListener {
    private ArrayList<RoutineModel> routineModels;
    private int selectedRoutineIndex;
    private int selectedWorkoutIndex;
    private RecyclerView recyclerView;
    private WorkoutEditAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String workoutName;
    private String duration;
    private String description;
    private EditText workoutNameEditText;
    private EditText workoutDescEditText;
    private ImageView commitChangesButton;
    private int imageViewSrcId = R.drawable.ic_checked_neutral_48dp;
    private DatabaseHelper db;
    private EditText durationEditText;
    private Spinner addExerciseSpinner;
    private ArrayList<ExerciseModel> spinnerExerciseModels;
    private Button createExerciseButton;
    private ArrayAdapter<ExerciseModel> exerciseAdapter;
    private int userId;
    private ResponseReceiver myReceiver;
    private LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_edit);

        db = new DatabaseHelper(this);
        db.openDB();

        routineModels = getIntent().getExtras().getParcelableArrayList("routine_models");
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        selectedRoutineIndex = sp.getInt("selected_routine_index", -1);
        selectedWorkoutIndex = sp.getInt("selected_workout_index", -1);
        userId = sp.getInt("UserId", -1);

        workoutName = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutName();
        duration = Double.toString(routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getEstimatedDuration());
        description = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getDescription();

        //Find Views
        loadingAnim = findViewById(R.id.workout_edit_loading_animation);
        workoutDescEditText = findViewById(R.id.edit_description_text_view);
        durationEditText = findViewById(R.id.edit_routine_duration_text_view);
        workoutNameEditText = findViewById(R.id.edit_routine_name_text_view);
        addExerciseSpinner = findViewById(R.id.activity_workout_edit_add_exercise_button);
        commitChangesButton = findViewById(R.id.workout_edit_activity_commit_changes);
        recyclerView = findViewById(R.id.edit_routine_recycler_view);

        loadingAnim.setVisibility(View.INVISIBLE);
        loadingAnim.cancelAnimation();

        workoutDescEditText.setText(description);
        workoutDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(description))
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                    imageViewSrcId = R.drawable.ic_checked_red_48dp;
                }
                else
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_neutral_48dp);
                    imageViewSrcId = R.drawable.ic_checked_neutral_48dp;
                }
            }
        });

        commitChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewSrcId == R.drawable.ic_checked_red_48dp)
                {
                    if (!loadingAnim.isAnimating())
                    {
                        loadingAnim.setVisibility(View.VISIBLE);
                        loadingAnim.playAnimation();
                        commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                        imageViewSrcId = R.drawable.ic_checked_red_48dp;
                    }
                    int workoutId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId();
                    int numExercises = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getNumberExercises();
                    String newWorkoutName = workoutNameEditText.getText().toString();
                    String newDesc = workoutDescEditText.getText().toString();

                    Double newDuration = null;
                    try {
                        newDuration = Double.parseDouble(durationEditText.getText().toString());
                        Intent updateWorkoutIntent = new Intent(WorkoutEditActivity.this, WorkoutService.class);
                        updateWorkoutIntent.putExtra("type", "update");
                        updateWorkoutIntent.putExtra("workoutId", workoutId);
                        updateWorkoutIntent.putExtra("description", newDesc);
                        updateWorkoutIntent.putExtra("workoutName", newWorkoutName);
                        updateWorkoutIntent.putExtra("duration", newDuration);
                        updateWorkoutIntent.putExtra("numExercises", numExercises);
                        startService(updateWorkoutIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Duration must be a number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        durationEditText.setText(duration);
        durationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(duration))
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                    imageViewSrcId = R.drawable.ic_checked_red_48dp;
                }
                else
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_neutral_48dp);
                    imageViewSrcId = R.drawable.ic_checked_neutral_48dp;
                }
            }
        });

        ((TextView)findViewById(R.id.edit_routine_name_text_view)).setText(workoutName);


        workoutNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(workoutName))
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                    imageViewSrcId = R.drawable.ic_checked_red_48dp;
                }
                else
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_neutral_48dp);
                    imageViewSrcId = R.drawable.ic_checked_neutral_48dp;
                }
            }
        });

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        addExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { return;}

                int workoutId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId();
                int numExercises = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getNumberExercises() + 1;

                commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                imageViewSrcId = R.drawable.ic_checked_red_48dp;
                Intent insertWorkoutExerciseIntent = new Intent(WorkoutEditActivity.this, WorkoutExerciseService.class);
                insertWorkoutExerciseIntent.putExtra("type", "insert");
                insertWorkoutExerciseIntent.putExtra("workoutId", workoutId);
                insertWorkoutExerciseIntent.putExtra("exerciseId", spinnerExerciseModels.get(position).getExerciseId());
                insertWorkoutExerciseIntent.putExtra("numExercises", numExercises);
                startService(insertWorkoutExerciseIntent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        createExerciseButton = findViewById(R.id.activity_workout_edit_create_new_exercise);
        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateExerciseDialog();
            }
        });

        initializeRecyclerView();
        initializeExerciseSpinner();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_workout_edit_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(WorkoutEditActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(WorkoutEditActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(WorkoutEditActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(WorkoutEditActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(WorkoutEditActivity.this, SettingsActivity.class);
                    break;

            }
            if (selectedActivity != null)
            {
                startActivity(selectedActivity);
            }

            return true;    //Means we want to select the clicked item
        }
    };

    private void initializeExerciseSpinner()
    {
        List<ExerciseModel> exerciseNames = null;

        if (userId == 1) //is admin
        {
            exerciseNames = db.getAllExercises();
        }
        else
        {
            exerciseNames = db.getAllExercisesForUser(userId);
        }

        if (exerciseNames == null)
        {
            spinnerExerciseModels = new ArrayList<>();
        }
        else
        {
            spinnerExerciseModels = (ArrayList<ExerciseModel>)exerciseNames;
        }

        spinnerExerciseModels.add(0, new ExerciseModel());
        exerciseAdapter = new ArrayAdapter<>(WorkoutEditActivity.this, android.R.layout.simple_list_item_1, spinnerExerciseModels);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addExerciseSpinner.setAdapter(exerciseAdapter);
    }

    private void initializeRecyclerView()
    {
        routineModels = RoutineModelHelper.populateRoutineModels(this, userId);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        adapter = new WorkoutEditAdapter(routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WorkoutEditAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openExerciseDetails(position);
            }
        });
    }

    private void openCreateExerciseDialog()
    {
        CreateExerciseDialog exerciseDialog = new CreateExerciseDialog();
        exerciseDialog.show(getSupportFragmentManager(), "createExerciseDialog");
    }

    private void openExerciseDetails(int position)
    {
        WorkoutEditDialog workoutEditDialog = new WorkoutEditDialog(position);
        workoutEditDialog.show(getSupportFragmentManager(), "workoutEditDialog");
    }

    @Override
    public void createExercise(String name, ArrayList<String> selectedMuscleGroups) {
        if (!loadingAnim.isAnimating())
        {
            loadingAnim.setVisibility(View.VISIBLE);
            loadingAnim.playAnimation();
            commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
            imageViewSrcId = R.drawable.ic_checked_red_48dp;
        }

        Intent insertExerciseAndMuscleIntent = new Intent(WorkoutEditActivity.this, ExerciseMuscleGroupService.class);
        insertExerciseAndMuscleIntent.putExtra("type", "insert");
        insertExerciseAndMuscleIntent.putExtra("exerciseName", name);
        insertExerciseAndMuscleIntent.putExtra("userId", userId);
        //Convert to String
        StringBuilder musclesString = new StringBuilder();
        for (int i = 0; i < selectedMuscleGroups.size(); i++)
        {
            musclesString.append(" ").append(selectedMuscleGroups.get(i));
        }
        insertExerciseAndMuscleIntent.putExtra("muscleGroupsString", musclesString.toString().trim());
        insertExerciseAndMuscleIntent.putExtra("muscleGroupsList", selectedMuscleGroups);
        startService(insertExerciseAndMuscleIntent);
    }

    @Override
    public void editExerciseDetails(int position, String minSets, String maxSets, String minReps, String maxReps, String weight) {
        int workoutId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId();
        int exerciseId = spinnerExerciseModels.get(position).getExerciseId();
        int workoutExerciseId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises().get(position).getWorkoutExerciseId();

        try {
            WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();
            workoutExerciseModel.setWorkoutExerciseId(workoutExerciseId);
            workoutExerciseModel.setMinimumSets(Integer.parseInt(minSets));
            workoutExerciseModel.setMinimumReps(Integer.parseInt(minReps));
            workoutExerciseModel.setMaximumSets(Integer.parseInt(maxSets));
            workoutExerciseModel.setMaximumReps(Integer.parseInt(maxReps));
            workoutExerciseModel.setIntensity(Double.parseDouble(weight));

            if (!loadingAnim.isAnimating())
            {
                loadingAnim.setVisibility(View.VISIBLE);
                loadingAnim.playAnimation();
                commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                imageViewSrcId = R.drawable.ic_checked_red_48dp;
            }
            Intent updateIntent = new Intent(WorkoutEditActivity.this, WorkoutExerciseService.class);
            updateIntent.putExtra("type", "update");
            updateIntent.putExtra("workoutId", workoutId);
            updateIntent.putExtra("exerciseId", exerciseId);
            updateIntent.putExtra("workoutExerciseModel", workoutExerciseModel);
            startService(updateIntent);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Must enter an integer for Sets and Reps or number for intensity", Toast.LENGTH_LONG).show();
        }
    }

    private void setReceiver() {
        myReceiver = new ResponseReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("workoutEditAction");
        intentFilter.addAction("workoutAction");
        intentFilter.addAction("errorWorkoutAction");
        intentFilter.addAction("exerciseAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("workoutEditAction"))
            {
                initializeRecyclerView();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("workoutAction"))
            {
                initializeRecyclerView();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("errorWorkoutAction"))
            {
                initializeRecyclerView();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("exerciseAction"))
            {
                initializeExerciseSpinner();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
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
