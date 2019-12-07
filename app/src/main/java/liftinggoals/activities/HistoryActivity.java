package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
    private ImageButton nextButton, previousButton;
    private CustomCalendarView customCalendarView;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.ENGLISH);
    private int userId;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        eventsRecyclerView = findViewById(R.id.activity_history_recycler_view);
        customCalendarView = findViewById(R.id.custom_calendar_view);
        previousButton = findViewById(R.id.calendarPreviousBtn);
        nextButton = findViewById(R.id.calendarNextBtn);
        customCalendarView.setupCalendar();

        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        userId = sp.getInt("UserId", -1);

        db = new DatabaseHelper(this);
        db.openDB();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalendarView.calendar.add(Calendar.MONTH, -1);
                eventsModels = (ArrayList<Event>) db.getEventsByMonthAndYear(userId, monthFormat.format(customCalendarView.calendar.getTime()), yearFormat.format(customCalendarView.calendar.getTime()));
                if (eventsModels == null) { eventsModels = new ArrayList<>(); }
                customCalendarView.setupCalendar();
                initializeRecyclerView();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalendarView.calendar.add(Calendar.MONTH, 1);
                eventsModels = (ArrayList<Event>) db.getEventsByMonthAndYear(userId, monthFormat.format(customCalendarView.calendar.getTime()), yearFormat.format(customCalendarView.calendar.getTime()));
                if (eventsModels == null) { eventsModels = new ArrayList<>(); }
                customCalendarView.setupCalendar();
                initializeRecyclerView();
            }
        });
        //Recycler View


        eventsModels = (ArrayList<Event>) db.getEventsByMonthAndYear(userId, monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));
        if (eventsModels == null) { eventsModels = new ArrayList<>(); }
        initializeRecyclerView();
        BottomNavigationView bottomNavigation = findViewById(R.id.activity_history_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private void initializeRecyclerView()
    {
        eventsRecyclerView.setHasFixedSize(true);
        eventsLayoutManager = new LinearLayoutManager(this);
        eventsAdapter = new EventsAdapter(eventsModels);

        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        eventsRecyclerView.setAdapter(eventsAdapter);
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
