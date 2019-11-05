package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        BottomNavigationView bottomNavigation = findViewById(R.id.activity_history_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(HistoryActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(HistoryActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    //selectedActivity = new Intent(ProgressActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(HistoryActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(HistoryActivity.this, SettingsActivity.class);
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
