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
    private CreateExerciseDialog exerciseDialog;
    private WorkoutEditDialog workoutEditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_edit);

        db = new DatabaseHelper(this);
        db.openDB();

        if (routineModels == null)
        {
            routineModels = getIntent().getExtras().getParcelableArrayList("routine_models");
        }
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

        if (!description.equals("Untitled Description"))
        {
            workoutDescEditText.setText(description);
        }
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

                    int workoutId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId();
                    int numExercises = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getNumberExercises();
                    String newWorkoutName = workoutNameEditText.getText().toString();
                    String newDesc = workoutDescEditText.getText().toString();

                    Double newDuration = null;
                    try {
                        newDuration = Double.parseDouble(durationEditText.getText().toString());

                        if (newDuration < 0)
                        {
                            Toast.makeText(getApplicationContext(), "You cannot enter negative value for duration!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!loadingAnim.isAnimating())
                        {
                            loadingAnim.setVisibility(View.VISIBLE);
                            loadingAnim.playAnimation();
                            commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                            imageViewSrcId = R.drawable.ic_checked_red_48dp;
                        }

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

        if (!duration.equals("0.0"))
        {
            durationEditText.setText(duration);
        }
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

        if (!workoutName.equals("Untitled Workout"))
        {
            workoutNameEditText.setText(workoutName);
        }
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

        addExerciseSpinner.setSelection(0);
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

        ExerciseModel placeHolder = new ExerciseModel();
        placeHolder.setExerciseName("Select an exercise...");
        spinnerExerciseModels.add(0, placeHolder);
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
                WorkoutExerciseModel currItem = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises().get(position);
                openExerciseDetails(position, currItem);
            }
        });
    }

    private void openCreateExerciseDialog()
    {
        exerciseDialog = new CreateExerciseDialog();
        exerciseDialog.show(getSupportFragmentManager(), "createExerciseDialog");
    }

    private void openExerciseDetails(int position, WorkoutExerciseModel item)
    {
        workoutEditDialog = new WorkoutEditDialog(position, item);
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
    public void editExerciseDetails(int position, WorkoutExerciseModel workoutExerciseModel) {
        workoutExerciseModel.setWorkoutId(routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId());
        workoutExerciseModel.setWorkoutExerciseId(routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises().get(position).getWorkoutExerciseId());

        if (!loadingAnim.isAnimating())
        {
            loadingAnim.setVisibility(View.VISIBLE);
            loadingAnim.playAnimation();
            commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
            imageViewSrcId = R.drawable.ic_checked_red_48dp;
        }
        Intent updateIntent = new Intent(WorkoutEditActivity.this, WorkoutExerciseService.class);
        updateIntent.putExtra("type", "update");
        updateIntent.putExtra("workoutExerciseModel", workoutExerciseModel);
        startService(updateIntent);
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
                initializeExerciseSpinner();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("workoutAction"))
            {
                initializeRecyclerView();
                initializeExerciseSpinner();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("errorWorkoutAction"))
            {
                initializeRecyclerView();
                initializeExerciseSpinner();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("exerciseAction"))
            {
                initializeExerciseSpinner();
                initializeExerciseSpinner();
                imageViewSrcId = R.drawable.ic_checked_green_48dp;
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("routineModels", routineModels);
        outState.putParcelableArrayList("spinnerExerciseModels", spinnerExerciseModels);
        outState.putParcelable("exerciseDialog", exerciseDialog);
        outState.putParcelable("workoutEditDialog", workoutEditDialog);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            routineModels = savedInstanceState.getParcelableArrayList("routineModels");
            spinnerExerciseModels = savedInstanceState.getParcelableArrayList("spinnerExerciseModels");
            exerciseDialog = savedInstanceState.getParcelable("exerciseDialog");
            workoutEditDialog = savedInstanceState.getParcelable("workoutEditDialog");
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
