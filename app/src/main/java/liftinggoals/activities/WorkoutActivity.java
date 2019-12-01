package liftinggoals.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import liftinggoals.adapters.WorkoutAdapter;
import liftinggoals.dialogs.DeleteRoutineDialog;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.RoutineModelHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.services.RoutineService;
import liftinggoals.services.WorkoutService;

public class WorkoutActivity extends AppCompatActivity implements DeleteRoutineDialog.DeleteDialogListener {
    private ArrayList<RoutineModel> routineModels;
    private int selectedRoutineIndex;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    private CardView createWorkout;
    private DatabaseHelper db;
    private int userId;
    private ImageView commitChangesImage;
    private ResponseReceiver myReceiver;

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

        commitChangesImage = findViewById(R.id.workout_activity_commit_button);
        recyclerView = findViewById(R.id.multiple_workout_recycler_view);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4)); //only do this once

        initializeActionSearch();
        initializeSwipe();

        createWorkout = findViewById(R.id.activity_workout_create_new_workout);
        createWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChangesImage.setImageResource(R.drawable.ic_checked_red_48dp);
                Intent intent = new Intent(WorkoutActivity.this, WorkoutService.class);
                intent.putExtra("type", "insert");
                intent.putExtra("routineId", routineModels.get(selectedRoutineIndex).getRoutineId());
                startService(intent);

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

    private void initializeActionSearch()
    {
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

    private void initializeSwipe()
    {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; //drag and drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                openDeleteDialog(pos, adapter.getItem(pos));
                adapter.delete(viewHolder, pos);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void openDeleteDialog(int position, Object item)
    {
        DeleteRoutineDialog deleteRoutineDialog = new DeleteRoutineDialog(position, item);
        deleteRoutineDialog.show(getSupportFragmentManager(), "deleteRoutineDialog");
    }

    @Override
    public void onCancelClicked(int position, Object item)
    {
        routineModels.get(selectedRoutineIndex).getWorkouts().add(position, (WorkoutModel) item);
        adapter.notifyItemInserted(routineModels.get(selectedRoutineIndex).getWorkouts().size()-1);
    }

    @Override
    public void onDeleteClicked(int position, Object item)
    {
        commitChangesImage.setImageResource(R.drawable.ic_checked_red_48dp);
        Intent intent = new Intent(this, WorkoutService.class);
        intent.putExtra("type", "delete");
        intent.putExtra("workoutId", ((WorkoutModel)item).getWorkoutId());
        intent.putExtra("routineId", routineModels.get(selectedRoutineIndex).getRoutineId());
        startService(intent);
    }

    private void setReceiver() {
        myReceiver = new ResponseReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("workoutAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("workoutAction"))
            {
                routineModels.get(selectedRoutineIndex).getWorkouts().add(new WorkoutModel("Untitled Workout", "Untitled Description", 0.0, 0));
                adapter.notifyItemInserted(routineModels.get(selectedRoutineIndex).getWorkouts().size()-1);
                initializeRecyclerView();
                initializeActionSearch();
                initializeSwipe();
                commitChangesImage.setImageResource(R.drawable.ic_checked_green_48dp);
            }
        }
    }

    @Override
    protected void onStart() {
        setReceiver();
        super.onStart();
        initializeRecyclerView();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onStop();
    }
}
