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
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import liftinggoals.adapters.EventsAdapter;
import liftinggoals.calendar.CustomCalendarView;
import liftinggoals.calendar.Event;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.models.HistoryModel;

public class HistoryActivity extends AppCompatActivity {
    private TextView t;
    private SimpleDateFormat sdf;
    private RecyclerView eventsRecyclerView;
    private RecyclerView.Adapter eventsAdapter;
    private RecyclerView.LayoutManager eventsLayoutManager;
    private ArrayList<Event> eventsModels;

    private CustomCalendarView customCalendarView;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.ENGLISH);
    private TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        customCalendarView = findViewById(R.id.custom_calendar_view);
        //Recycler View

        DatabaseHelper db = new DatabaseHelper(this);
        db.openDB();

        eventsModels = (ArrayList<Event>) db.getEventsByMonthAndYear(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        if (eventsModels == null)
        {
            eventsModels = new ArrayList<>();
        }

        eventsRecyclerView = findViewById(R.id.activity_history_recycler_view);
        eventsRecyclerView.setHasFixedSize(true);
        eventsLayoutManager = new LinearLayoutManager(this);
        eventsAdapter = new EventsAdapter(eventsModels);

        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        eventsRecyclerView.setAdapter(eventsAdapter);


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
