package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.liftinggoals.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.data.WorkoutExercisesTable;

public class ExerciseActivity extends AppCompatActivity {
    private LineChart lineChart;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        button = findViewById(R.id.activity_exercise_done_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //this leaves the keyboard hidden on load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Spinner exerciseSpinner = findViewById(R.id.exercise_selection);

        ArrayList<WorkoutExerciseModel> list = getIntent().getExtras().getParcelableArrayList("exercise_list");
        ArrayList<String> exerciseNames = new ArrayList<>();
        for (WorkoutExerciseModel e : list)
        {
            exerciseNames.add(e.getExercise().getExerciseName());
        }
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_list_item_1, exerciseNames);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);

        lineChart = findViewById(R.id.line_graph);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        ArrayList<Entry> NoOfEmp = new ArrayList<>();

        NoOfEmp.add(new Entry(0,945f));
        NoOfEmp.add(new Entry(1,1040f));
        NoOfEmp.add(new Entry(2,1133f));
        NoOfEmp.add(new Entry(3,1240f));
        NoOfEmp.add(new Entry(4,1369f));
        NoOfEmp.add(new Entry(5,1487f));
        NoOfEmp.add(new Entry(6,1501f));
        NoOfEmp.add(new Entry(7,1645f));
        NoOfEmp.add(new Entry(8,1578f));
        NoOfEmp.add(new Entry(9,1695f));

        LineDataSet lineDataSet = new LineDataSet(NoOfEmp, "Weight");
        lineDataSet.setFillAlpha(110);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(3f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);

        lineChart.setVisibility(View.VISIBLE);
        lineChart.animateY(2000);
        lineChart.setData(data);
        lineChart.invalidate();

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

    }
}
