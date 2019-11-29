package liftinggoals.activities;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
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

public class WorkoutEditActivity extends AppCompatActivity implements WorkoutEditDialog.WorkoutEditDialogListener, CreateExerciseDialog.CreateExerciseDialogListener {
    private ArrayList<RoutineModel> routineModels;
    private int selectedRoutineIndex;
    private int selectedWorkoutIndex;
    private RecyclerView recyclerView;
    private WorkoutEditAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String workoutName;
    private String duration;
    private EditText workoutNameEditText;
    private ImageView commitChangesButton;
    private int imageViewSrcId = R.drawable.ic_checked_neutral_48dp;
    private DatabaseHelper db;
    private EditText durationEditText;
    private Spinner addExerciseSpinner;
    private ArrayList<ExerciseModel> spinnerExerciseModels;
    private Button createExerciseButton;
    private ArrayAdapter<ExerciseModel> exerciseAdapter;
    private int userId;


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
        System.out.println("Inside workoutEditActivity- selectedWorkoutIndex: " + selectedWorkoutIndex);

        workoutName = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutName();
        duration = Double.toString(routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getEstimatedDuration());

        //Find Views
        durationEditText = findViewById(R.id.edit_routine_duration_text_view);
        workoutNameEditText = findViewById(R.id.edit_routine_name_text_view);
        addExerciseSpinner = findViewById(R.id.activity_workout_edit_add_exercise_button);
        commitChangesButton = findViewById(R.id.workout_edit_activity_commit_changes);
        recyclerView = findViewById(R.id.edit_routine_recycler_view);

        commitChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewSrcId == R.drawable.ic_checked_red_48dp)
                {
                    String newWorkoutName = workoutNameEditText.getText().toString();
                    String [] split = durationEditText.getText().toString().split(" ");
                    db.updateWorkout(routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises().get(0).getWorkoutExerciseId(),
                            newWorkoutName, Double.valueOf(split[0].trim()));
                    routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).setWorkoutName(newWorkoutName);

                    commitChangesButton.setImageResource(R.drawable.ic_checked_green_48dp);
                    imageViewSrcId = R.drawable.ic_checked_green_48dp;
                }
            }
        });

        durationEditText.setText(duration + " minutes");
        durationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(duration + " minutes"))
                {
                    commitChangesButton.setImageResource(R.drawable.ic_checked_red_48dp);
                    imageViewSrcId = R.drawable.ic_checked_red_48dp;
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
            }
        });

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        addExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { return;}

                int workoutId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId();
                int numExercises = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getNumberExercises() + 1;

                WorkoutExerciseModel newWorkoutExercise = new WorkoutExerciseModel();
                newWorkoutExercise.setWorkoutId(workoutId);
                newWorkoutExercise.setExercise(spinnerExerciseModels.get(position));
                newWorkoutExercise.setIntensity(0);
                newWorkoutExercise.setMinimumSets(0);
                newWorkoutExercise.setMinimumReps(0);
                newWorkoutExercise.setMaximumSets(0);
                newWorkoutExercise.setMaximumReps(0);

                long insertResult = db.insertWorkoutExercise(workoutId, spinnerExerciseModels.get(position).getExerciseId(), 0,0,0,0,0);
                routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises().add(newWorkoutExercise);
                db.updateWorkoutNumExercises(workoutId, numExercises);
                initializeRecyclerView();
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


        List<ExerciseModel> exerciseNames = db.getAllExercises();
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

        initializeRecyclerView();

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
    public void applyChanges(String name, ArrayList<String> selectedMuscleGroups) {
        long insertedPK = db.insertExercise(name);
        for (String muscleGroup : selectedMuscleGroups)
        {
            db.insertMuscleGroup((int)insertedPK, muscleGroup);
        }

        spinnerExerciseModels.add(new ExerciseModel((int)insertedPK, name));
        exerciseAdapter.notifyDataSetChanged();
        addExerciseSpinner.postInvalidate();
        addExerciseSpinner.setAdapter(exerciseAdapter);
    }

    @Override
    public void applyChanges(int position, String minSets, String maxSets, String minReps, String maxReps, String weight) {
        int exerciseId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getExercises().get(position).getExercise().getExerciseId();
        int workoutId = routineModels.get(selectedRoutineIndex).getWorkouts().get(selectedWorkoutIndex).getWorkoutId();
        int workoutExerciseId = db.getWorkoutExerciseByWorkoutAndExerciseId(workoutId, exerciseId).getWorkoutExerciseId();
        try {
            db.updateWorkoutExcercise(workoutExerciseId, Integer.parseInt(minSets), Integer.parseInt(minReps), Integer.parseInt(maxSets), Integer.parseInt(maxReps), Double.parseDouble(weight));
            initializeRecyclerView();
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Must enter an integer for Sets and Reps or number for intensity", Toast.LENGTH_LONG).show();
        }
    }
}
