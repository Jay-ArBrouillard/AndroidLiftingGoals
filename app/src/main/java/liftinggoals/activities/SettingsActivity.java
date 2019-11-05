package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BottomNavigationView bottomNavigation = findViewById(R.id.activity_settings_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(SettingsActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(SettingsActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(SettingsActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(SettingsActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    //selectedActivity = new Intent(SettingsActivity.this, SettingsActivity.class);
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
