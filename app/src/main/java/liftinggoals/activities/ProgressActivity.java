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

import liftinggoals.adapters.ExerciseAdapter;
import liftinggoals.adapters.VolumeAdapter;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.misc.VolumeGroupItem;

public class ProgressActivity extends AppCompatActivity {
    public ArrayList<VolumeGroupItem> volumeGroups;
    private RecyclerView volumeRecyclerView;
    private RecyclerView.Adapter volumeAdapter;
    private RecyclerView.LayoutManager volumeLayoutManager;
    public ArrayList<ExerciseModel> exercisesList;
    private RecyclerView exerciseRecyclerView;
    private RecyclerView.Adapter exerciseAdapter;
    private RecyclerView.LayoutManager exerciseLayoutManager;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        volumeGroups = new ArrayList<>();
        exercisesList = new ArrayList<>();

        initializeRecyclerView();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_progress_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private void initializeRecyclerView() {
        volumeRecyclerView = findViewById(R.id.progress_activity_volume_groups_recycler_view);
        volumeRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Arms"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Back"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Biceps"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Chest"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Hamstrings"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Legs"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Quads"));
        volumeGroups.add(new VolumeGroupItem(R.drawable.ic_keyboard_arrow_right_white_24dp, "Triceps"));

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
        exerciseAdapter = new ExerciseAdapter(exercisesList);
        exerciseRecyclerView.setLayoutManager(exerciseLayoutManager);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

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
