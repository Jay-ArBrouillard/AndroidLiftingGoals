package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.liftinggoals.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import liftinggoals.fragments.HistoryFragment;
import liftinggoals.fragments.MapsFragment;
import liftinggoals.fragments.ProgressFragment;
import liftinggoals.fragments.RoutineFragment;
import liftinggoals.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), new RoutineFragment()).commit();
    }

    public void AddRoutineButton(View view) {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedFragment = new RoutineFragment();
                    break;
                case R.id.nav_progress:
                    selectedFragment = new ProgressFragment();
                    break;
                case R.id.nav_history:
                    selectedFragment = new HistoryFragment();
                    break;
                case R.id.nav_maps:
                    selectedFragment = new MapsFragment();
                    break;
                case R.id.nav_settings:
                    selectedFragment = new SettingFragment();
                    break;

            }
            if (selectedFragment != null)
            {
                getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), selectedFragment).commit();

            }

            return true;    //Means we want to select the clicked item
        }
    };
}
