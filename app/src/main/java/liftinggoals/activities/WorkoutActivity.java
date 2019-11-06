package liftinggoals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import liftinggoals.adapters.WorkoutAdapter;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.classes.WorkoutModel;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class WorkoutActivity extends AppCompatActivity {
    private ArrayList<WorkoutModel> workoutsList;
//    private ArrayList<String> workoutItems;
//    private ArrayList<ExerciseModel> exerciseList;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //Get info from Intent->Bundle
        workoutsList = getIntent().getExtras().getParcelableArrayList("workout_item");
        TextView title = findViewById(R.id.fragment_multiple_workout_title);
        title.setText(getIntent().getExtras().getString("routine_name"));

        recyclerView = findViewById(R.id.multiple_workout_recycler_view);
        initializeRecyclerView();
        initializeActionSearch();
        initializeSwipe();

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

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(this);

        adapter = new WorkoutAdapter(workoutsList);
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent startExerciseActivity = new Intent(WorkoutActivity.this, ExerciseActivity.class);
                ArrayList<WorkoutExerciseModel> temp =  workoutsList.get(position).getExercises();
                startExerciseActivity.putParcelableArrayListExtra("exercise_list", temp);
                startActivity(startExerciseActivity);
            }

            @Override
            public void onItemEdit(int position) {
                Intent editWorkoutActivity = new Intent(WorkoutActivity.this, WorkoutEditActivity.class);
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
}
