package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import liftinggoals.adapters.ProgressAdapter;
import liftinggoals.adapters.VolumeAdapter;
import liftinggoals.classes.ExerciseLogModel;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.ProgressExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.classes.VolumeGroupModel;

public class ProgressActivity extends AppCompatActivity {
    private ArrayList<VolumeGroupModel> volumeGroups;
    private RecyclerView volumeRecyclerView;
    private RecyclerView.Adapter volumeAdapter;
    private RecyclerView.LayoutManager volumeLayoutManager;
    private ArrayList<ExerciseModel> exercisesList;
    private RecyclerView exerciseRecyclerView;
    private ProgressAdapter exerciseAdapter;
    private RecyclerView.LayoutManager exerciseLayoutManager;
    private SearchView search;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        volumeGroups = new ArrayList<>();
        exercisesList = new ArrayList<>();

        db = new DatabaseHelper(this);
        db.openDB();

        initializeRecyclerView();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_progress_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private void initializeRecyclerView() {
        volumeRecyclerView = findViewById(R.id.progress_activity_volume_groups_recycler_view);
        volumeRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Arms"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Back"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Biceps"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Chest"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Hamstrings"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Legs"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Quadriceps"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Trapezius"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Triceps"));

        volumeLayoutManager = new LinearLayoutManager(this);
        volumeAdapter = new VolumeAdapter(volumeGroups);
        volumeRecyclerView.setLayoutManager(volumeLayoutManager);
        volumeRecyclerView.setAdapter(volumeAdapter);


        exerciseRecyclerView = findViewById(R.id.activity_progress_exercises_recycler_view);
        exerciseRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();
        exercisesList = (ArrayList<ExerciseModel>) db.getAllExercises();

        exerciseLayoutManager = new LinearLayoutManager(this);
        exerciseAdapter = new ProgressAdapter(exercisesList);
        exerciseRecyclerView.setLayoutManager(exerciseLayoutManager);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        exerciseAdapter.setOnItemClickListener(new ProgressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ProgressActivity.this, ProgressGraphActivity.class);
                intent.putExtra("exercise_id", exercisesList.get(position).getExerciseId());
                intent.putExtra("exercise_name", exercisesList.get(position).getExerciseName());
                startActivity(intent);
            }
        });

        db.closeDB();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(ProgressActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    //selectedActivity = new Intent(ProgressActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(ProgressActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(ProgressActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(ProgressActivity.this, SettingsActivity.class);
                    break;

            }
            if (selectedActivity != null)
            {
                startActivity(selectedActivity);
            }

            return true;    //Means we want to select the clicked item
        }
    };
}
