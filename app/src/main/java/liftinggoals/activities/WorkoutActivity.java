package liftinggoals.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import liftinggoals.adapters.WorkoutAdapter;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.RoutineModelHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class WorkoutActivity extends AppCompatActivity {
    private ArrayList<RoutineModel> routineModels;
    private int selectedRoutineIndex;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    private CardView createWorkout;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        db = new DatabaseHelper(this);
        db.openDB();

        routineModels = getIntent().getExtras().getParcelableArrayList("routine_models");
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        userId = sp.getInt("UserId", -1);
        selectedRoutineIndex = sp.getInt("selected_routine_index", -1);

        TextView title = findViewById(R.id.fragment_multiple_workout_title);
        title.setText(routineModels.get(selectedRoutineIndex).getRoutineName());   //Set Workout Title

        recyclerView = findViewById(R.id.multiple_workout_recycler_view);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4)); //only do this once

        initializeActionSearch();
        initializeSwipe();

        createWorkout = findViewById(R.id.activity_workout_create_new_workout);
        createWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long insertedPK = db.insertWorkout("Untitled Workout", "Empty Description", 0, 0);
                db.insertRoutineWorkout(routineModels.get(selectedRoutineIndex).getRoutineId(), (int)insertedPK);
                routineModels.get(selectedRoutineIndex).getWorkouts().add(new WorkoutModel("Untitled Workout", "Empty Description", 0, 0));
                adapter.notifyDataSetChanged();
                ((ImageView)findViewById(R.id.workout_activity_commit_button)).setImageResource(R.drawable.ic_checked_green_48dp);
            }
        });


        BottomNavigationView bottomNavigation = findViewById(R.id.activity_workout_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(WorkoutActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(WorkoutActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(WorkoutActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(WorkoutActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(WorkoutActivity.this, SettingsActivity.class);
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
        routineModels =  RoutineModelHelper.populateRoutineModels(this, userId);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        adapter = new WorkoutAdapter(routineModels.get(selectedRoutineIndex).getWorkouts());
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent startExerciseActivity = new Intent(WorkoutActivity.this, ExerciseActivity.class);
                startExerciseActivity.putParcelableArrayListExtra("workout_exercise_models", routineModels.get(selectedRoutineIndex).getWorkouts().get(position).getExercises());
                startExerciseActivity.putExtra("workout_name", routineModels.get(selectedRoutineIndex).getWorkouts().get(position).getWorkoutName());
                SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
                sp.edit().putInt("selected_workout_index", position).commit();
                startActivity(startExerciseActivity);
            }

            @Override
            public void onItemEdit(int position) {
                Intent editWorkoutActivity = new Intent(WorkoutActivity.this, WorkoutEditActivity.class);
                editWorkoutActivity.putParcelableArrayListExtra("routine_models", routineModels);
                SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
                sp.edit().putInt("selected_workout_index", position).commit();
                System.out.println("selectedWorkoutIndex: " + position);
                startActivity(editWorkoutActivity);
            }

        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initializeActionSearch() {
        //Search
        search = findViewById(R.id.fragment_multiple_workout_action_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        //Make the entire text box clickable aswell
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.onActionViewExpanded();
            }
        });
    }

    private void initializeSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; //drag and drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                adapter.delete(viewHolder, pos);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeRecyclerView();
    }
}
