package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.liftinggoals.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import liftinggoals.adapters.ProgressExerciseAdapter;
import liftinggoals.classes.ExerciseLogModel;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.ProgressExerciseModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class ProgressGraphActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int exerciseId;
    private String exerciseName;
    private RecyclerView progressRecyclerView;
    private RecyclerView.Adapter progressAdapter;
    private RecyclerView.LayoutManager progressLayoutManager;
    private ArrayList<ExerciseLogModel> exerciseLogModels;
    private LineChart lineChart;
    private ArrayList<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_graph);
        exerciseId = getIntent().getIntExtra("exercise_id", -1);
        exerciseName = getIntent().getStringExtra("exercise_name");
        ((TextView)(findViewById(R.id.activity_progress_graph_title))).setText(exerciseName);

        db = new DatabaseHelper(this);
        db.openDB();


        List<ExerciseLogModel> temp = db.getExercisesLogsByExerciseId(exerciseId);
        if (temp == null || temp.size() == 0)
        {
            exerciseLogModels = new ArrayList<>();
        }
        else
        {
            exerciseLogModels = (ArrayList<ExerciseLogModel>) db.getExercisesLogsByExerciseId(exerciseId);
        }

        ArrayList<ProgressExerciseModel> exerciseModels = new ArrayList<>();
        for (int i = 0; i < exerciseLogModels.size(); i++)
        {
            ProgressExerciseModel newItem = new ProgressExerciseModel();
            String iValue = (i+1) + ". ";
            newItem.setIndex(iValue);
            newItem.setExerciseName(exerciseName);
            StringBuilder specsBuilder = new StringBuilder();
            newItem.setSpecs(processString(exerciseLogModels.get(i), specsBuilder));
            newItem.setTime(formatDateTime(this, exerciseLogModels.get(i).getDate()));

            exerciseModels.add(newItem);
        }

        progressRecyclerView = findViewById(R.id.activity_progress_graph_recycler_view);
        progressRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));
        progressRecyclerView.setHasFixedSize(true);


        progressLayoutManager = new LinearLayoutManager(this);
        progressAdapter = new ProgressExerciseAdapter(exerciseModels);

        progressRecyclerView.setLayoutManager(progressLayoutManager);
        progressRecyclerView.setAdapter(progressAdapter);

        calculateGeneralStatistics();
        buildLineGraph();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_progress_graph_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }

    private void calculateGeneralStatistics()
    {
        //Volume
        double volume = 0;
        double highestWeight = 0;
        double reps = 0;
        int totalSets = 0;
        int totalReps = 0;
        for (ExerciseLogModel e : exerciseLogModels)
        {
            volume += e.getIntensity() * e.getRepsPerformed();
            if (e.getIntensity() > highestWeight)
            {
                highestWeight = e.getIntensity();
                reps = e.getRepsPerformed();
            }

            totalSets++;
            totalReps += e.getRepsPerformed();
        }

        ((TextView)(findViewById(R.id.activity_progress_graph_volume_value))).setText(Double.toString(volume) + " LB");


        double max =  highestWeight / (1.0278 - 0.0278 * reps);

        if (reps == 1)
        {
            ((TextView)(findViewById(R.id.activity_progress_graph_estimated_1rm_value))).setText(Double.toString(Math.round(highestWeight)) + " LB");
            ((TextView)(findViewById(R.id.activity_progress_graph_1rm_value))).setText(Double.toString(Math.round(highestWeight)) + " LB");
        }
        else
        {
            ((TextView)(findViewById(R.id.activity_progress_graph_estimated_1rm_value))).setText(Double.toString(Math.round(max)) + " LB");
            ((TextView)(findViewById(R.id.activity_progress_graph_1rm_value))).setText(Double.toString(Math.round(highestWeight)) + " LB");
        }

        ((TextView)(findViewById(R.id.activity_progress_graph_sets_value))).setText(Integer.toString(totalSets));
        ((TextView)(findViewById(R.id.activity_progress_graph_reps_value))).setText(Integer.toString(totalReps));
    }

    private String processString(ExerciseLogModel e, StringBuilder stringBuilder)
    {
        int set = e.getSetPerformed();
        int reps = e.getRepsPerformed();
        double weight = e.getIntensity();
        double rpe = e.getRpe();

        stringBuilder.append(weight).append(" LB");
        stringBuilder.append(" x ");
        stringBuilder.append(set);
        stringBuilder.append(reps);

        if (rpe != -1 && rpe != 0)
        {
            stringBuilder.append(" (").append(rpe).append(")");
        }

        return stringBuilder.toString();
    }

    private void buildLineGraph()
    {
        lineChart = findViewById(R.id.activity_progress_graph_line_chart);

        for (ExerciseLogModel logModel : exerciseLogModels)
        {
            String reps = Integer.toString(logModel.getRepsPerformed());
            String intensity = Double.toString(logModel.getIntensity());

            entries.add(new Entry(Float.valueOf(reps), Float.valueOf(intensity)));
        }

        Collections.sort(entries, new EntryXComparator());

        lineChart.setScaleEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setDrawMarkers(false);

        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.enableDashedLine(10,8,15);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);

        lineChart.setVisibility(View.VISIBLE);
        lineChart.animateY(100);
        lineChart.setData(data);
        lineChart.invalidate();

        //lineChart.setBackgroundColor(Color.WHITE);
        //lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        //lineChart.setDrawGridBackground(false);

        lineChart.setDrawBorders(true);
        lineChart.setPinchZoom(false);

        //Axes settings
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisRight = lineChart.getAxisRight();
        YAxis yAxisLeft = lineChart.getAxisLeft();

        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        yAxisLeft.setGranularity(5f);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisRight.setGranularity(5f);
        yAxisRight.setTextColor(Color.WHITE);

        Description description = new Description();
        description.setText("Log an exercise to see data");
        lineChart.setDescription(description);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(ProgressGraphActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(ProgressGraphActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(ProgressGraphActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(ProgressGraphActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(ProgressGraphActivity.this, SettingsActivity.class);
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
