package com.example.liftinggoals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if coming from Google maps activity
        Intent intent = getIntent();
        String strMenuId = intent.getStringExtra("menu_id");

        if (strMenuId != null) {
            Fragment selectedFragment = null;
            switch (Integer.parseInt(strMenuId)) {
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
                    Intent mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(mapsIntent);
                    break;
                case R.id.nav_settings:
                    selectedFragment = new SettingFragment();
                    break;

            }
            if (selectedFragment != null)
            {
                getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), selectedFragment).commit();

            }
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        //Get Intent info. from LoginActivity
//Intent intent = getIntent();
//        username = intent.getStringExtra("username");

        //Now pass username from Activity to a fragment
//        Bundle bundle = new Bundle();
//        bundle.putString("username", username);
        RoutineFragment startingFragment = new RoutineFragment();
//        startingFragment.setArguments(bundle);
        
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, startingFragment).commit();

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
                    Intent mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(mapsIntent);
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
