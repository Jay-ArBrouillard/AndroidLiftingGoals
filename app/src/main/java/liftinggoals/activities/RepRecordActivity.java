package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import liftinggoals.adapters.RecordsAdapter;
import liftinggoals.models.RecordModel;
import liftinggoals.data.DatabaseHelper;

public class RepRecordActivity extends AppCompatActivity {
    private RecyclerView recordsRecyclerView;
    private RecyclerView.Adapter recordsAdapter;
    private RecyclerView.LayoutManager recordsLayoutManager;
    private ArrayList<RecordModel> recordModels;
    private String exerciseName;
    private int exerciseId;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_record);
        exerciseId = getIntent().getIntExtra("exercise_id", -1);
        exerciseName = getIntent().getStringExtra("exercise_name");
        ((TextView) findViewById(R.id.activity_rep_record_exercise_name)).setText(exerciseName);

        db = new DatabaseHelper(this);
        db.openDB();

        ArrayList<RecordModel> recordModels = (ArrayList<RecordModel> ) db.getRecordsByExerciseId(exerciseId);
        if (recordModels == null)
        {
            recordModels = new ArrayList<>();
        }

        Collections.sort(recordModels, new Comparator<RecordModel>() {
            @Override
            public int compare(RecordModel o1, RecordModel o2) {
                return  o1.getRepsPerformed() - o2.getRepsPerformed();
            }
        });

        recordsRecyclerView = findViewById(R.id.rep_record_recycler_view);
        recordsRecyclerView.setHasFixedSize(true);
        recordsLayoutManager = new LinearLayoutManager(this);
        recordsAdapter = new RecordsAdapter(recordModels);

        recordsRecyclerView.setLayoutManager(recordsLayoutManager);
        recordsRecyclerView.setAdapter(recordsAdapter);

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_rep_record_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(RepRecordActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(RepRecordActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(RepRecordActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(RepRecordActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(RepRecordActivity.this, SettingsActivity.class);
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
