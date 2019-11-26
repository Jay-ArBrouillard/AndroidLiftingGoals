package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import liftinggoals.adapters.ProgressAdapter;
import liftinggoals.adapters.VolumeAdapter;
import liftinggoals.models.ExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.models.VolumeGroupModel;

public class ProgressActivity extends AppCompatActivity {
    private ArrayList<VolumeGroupModel> volumeGroups;
    private RecyclerView volumeRecyclerView;
    private VolumeAdapter volumeAdapter;
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
        initializeActionSearches();
        CardView overallCard = findViewById(R.id.activity_progress_overall_cardview);
        overallCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this, ProgressGraphActivity.class);
                intent.putExtra("overall", true);
                intent.putExtra("exercise_name", "All Exercises");
                startActivity(intent);
            }
        });


        BottomNavigationView bottomNavigation = findViewById(R.id.activity_progress_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private void initializeActionSearches() {
        search = findViewById(R.id.activity_progress_action_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                volumeAdapter.getFilter().filter(newText);
                exerciseAdapter.getFilter().filter(newText);
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

    private void initializeRecyclerView() {
        volumeRecyclerView = findViewById(R.id.progress_activity_volume_groups_recycler_view);
        volumeRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));


        //TODO database stuff with volumes groups so you can click on them in progress tab
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Arms"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Back"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Biceps"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Calves"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Chest"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Erector Spinae"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Forearms"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Glutes"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Hamstrings"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Lats"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Neck"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Quadriceps"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Trapezius"));
        volumeGroups.add(new VolumeGroupModel(R.drawable.ic_keyboard_arrow_right_white_24dp, "Triceps"));

        volumeLayoutManager = new LinearLayoutManager(this);
        volumeAdapter = new VolumeAdapter(volumeGroups);
        volumeRecyclerView.setLayoutManager(volumeLayoutManager);
        volumeRecyclerView.setAdapter(volumeAdapter);

        volumeAdapter.setOnItemClickListener(new VolumeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ProgressActivity.this, ProgressGraphActivity.class);
                intent.putExtra("volume_group_name", volumeGroups.get(position).getVolumeGroup());
                startActivity(intent);
            }
        });

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
