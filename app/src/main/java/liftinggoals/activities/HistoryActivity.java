package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.liftinggoals.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import liftinggoals.adapters.HistoryAdapter;
import liftinggoals.calendar.CustomCalendarView;
import liftinggoals.models.HistoryItem;
import liftinggoals.models.WorkoutModel;
import liftinggoals.data.DatabaseHelper;

public class HistoryActivity extends AppCompatActivity {
    private CompactCalendarView calendar;
    private TextView t;
    private SimpleDateFormat sdf;
    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter historyAdapter;
    private RecyclerView.LayoutManager historyLayoutManager;
    private ArrayList<HistoryItem>  historyItems;

    private CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        customCalendarView = findViewById(R.id.custom_calendar_view);
/*

        calendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        String[] d = {"","","","","","",""};
        calendar.setDayColumnNames(d);

        t = (TextView) findViewById(R.id.history_fragment_workout_details);

        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/avebetwan.ttf");
        sdf = new SimpleDateFormat("MMMM");

        t.setText(sdf.format(calendar.getFirstDayOfCurrentMonth()));
        //t.setTypeface(custom_font);

        Date date1 = new Date(117,7,15);
        Event e = new Event(Color.DKGRAY, date1.getTime(),"text1");
        calendar.addEvent(e);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                t.setText(sdf.format(calendar.getFirstDayOfCurrentMonth()));
            }
        });

        //Recycler View
        DatabaseHelper db = new DatabaseHelper(this);
        db.openDB();
        historyItems = new ArrayList<>();
        for (WorkoutModel workoutModel : db.getAllWorkouts()) //Fix
        {
            String workoutName = workoutModel.getWorkoutName();
            HistoryItem historyItem = new HistoryItem(workoutName, "No Data", "No Date data");
            historyItems.add(historyItem);
        }

        historyRecyclerView = findViewById(R.id.activity_history_recycler_view);
        historyRecyclerView.setHasFixedSize(true);
        historyLayoutManager = new LinearLayoutManager(this);
        historyAdapter = new HistoryAdapter(historyItems);

        historyRecyclerView.setLayoutManager(historyLayoutManager);
        historyRecyclerView.setAdapter(historyAdapter);


        BottomNavigationView bottomNavigation = findViewById(R.id.activity_history_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);*/
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
