package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import liftinggoals.adapters.WorkoutEditAdapter;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class WorkoutEditActivity extends AppCompatActivity {
    public ArrayList<WorkoutExerciseModel> workoutExerciseModels;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String workoutName;
    private String duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_edit);

        workoutName = getIntent().getExtras().getString("workout_name");
        duration = Double.toString(getIntent().getExtras().getDouble("workout_duration"));

        ((TextView)findViewById(R.id.edit_routine_name_text_view)).setText(workoutName);
        ((TextView)findViewById(R.id.edit_routine_duration_text_view)).setText(duration + " minutes");

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
        recyclerView = findViewById(R.id.edit_routine_recycler_view);
        workoutExerciseModels = getIntent().getExtras().getParcelableArrayList("exercise_list");
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        adapter = new WorkoutEditAdapter(workoutExerciseModels);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
