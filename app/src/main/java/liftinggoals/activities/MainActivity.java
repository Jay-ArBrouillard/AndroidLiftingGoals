package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.liftinggoals.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import liftinggoals.classes.RoutineModel;
import liftinggoals.fragments.HistoryFragment;
import liftinggoals.fragments.MapsFragment;
import liftinggoals.fragments.ProgressFragment;
import liftinggoals.fragments.RoutineFragment;
import liftinggoals.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private RoutineFragment startingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new RoutineFragment()).addToBackStack(null).commit();
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
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
//                fragmentManager.beginTransaction().replace((R.id.fragment_container), selectedFragment).commit();
//                fragmentManager.beginTransaction().addToBackStack(null);
            }

            return true;    //Means we want to select the clicked item
        }
    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "startingFragment", startingFragment);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        startingFragment = (RoutineFragment)getSupportFragmentManager().getFragment(savedInstanceState, "startingFragment");
    }
}
