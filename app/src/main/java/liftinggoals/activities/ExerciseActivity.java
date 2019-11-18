package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Collections;

import liftinggoals.classes.ExerciseLogModel;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.data.DatabaseHelper;

public class ExerciseActivity extends AppCompatActivity {
    private LineChart lineChart;
    private Button doneButton;
    private Button logButton;
    private ArrayList<WorkoutExerciseModel> exerciseList;
    private DatabaseHelper db;
    private ArrayList<Entry> entries = new ArrayList<>();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        exerciseList = getIntent().getExtras().getParcelableArrayList("exercise_list");
        final Spinner exerciseSpinner = findViewById(R.id.exercise_activity_spinner);
        db = new DatabaseHelper(this);
        db.openDB();


        buildLineGraph(exerciseList.get(0));

        doneButton = findViewById(R.id.activity_exercise_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                startActivity(intent);
            }
        });

        logButton = findViewById(R.id.exercise_activity_log_button);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseLogModel exerciseLogModel = new ExerciseLogModel();
                exerciseLogModel.setWorkoutExeriseId(exerciseList.get(exerciseSpinner.getSelectedItemPosition()).getWorkoutExerciseId());
                TextView set = findViewById(R.id.exercise_activity_set_value);
                EditText reps = findViewById(R.id.exercise_activity_reps_value);
                EditText weight = findViewById(R.id.exercise_activity_weight_value);

                try {
                    int setInteger = Integer.parseInt(set.getText().toString());
                    exerciseLogModel.setSetPerformed(setInteger);
                    exerciseLogModel.setRepsPerformed(Integer.parseInt(reps.getText().toString().trim()));
                    exerciseLogModel.setIntensity(Double.parseDouble(weight.getText().toString().trim()));

                    setInteger++;
                    set.setText(Integer.toString(setInteger));

                    db.insertExerciseLog(exerciseLogModel);
                    buildLineGraph(exerciseList.get(exerciseSpinner.getSelectedItemPosition()));

                    //TODO insert in  Remote database
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(), "Type in a valid value for weight and reps", Toast.LENGTH_LONG).show();
                }
            }
        });

        ArrayList<String> exerciseNames = new ArrayList<>();
        for (WorkoutExerciseModel e : exerciseList)
        {
            exerciseNames.add(processString(e, new StringBuilder()));
        }
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_list_item_1, exerciseNames);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);

        spinner = findViewById(R.id.exercise_activity_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WorkoutExerciseModel selected =  exerciseList.get(position);
                buildLineGraph(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

                //this leaves the keyboard hidden on load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void logButtonEvent()
    {

    }

    private String processString(WorkoutExerciseModel e, StringBuilder stringBuilder)
    {
        ExerciseModel exerciseModel = e.getExercise();
        stringBuilder.append(exerciseModel.getExerciseName()).append(" (");
        int minSets = e.getMinimumSets();
        int maxSets = e.getMaximumSets();
        int minReps = e.getMinimumReps();
        int maxReps = e.getMaximumReps();

        if (minSets == -1 && maxSets != -1)
        {
            stringBuilder.append(maxSets).append(" Sets");
        }
        else if (minSets != -1 && maxSets == -1)
        {
            stringBuilder.append(minSets).append("+").append(" Sets");
        }
        else if (minSets != -1 && maxSets != -1)
        {
            stringBuilder.append(minSets).append("-").append(maxSets).append(" Sets");
        }

        stringBuilder.append(", ");

        if (minReps == -1 && maxReps != -1)
        {
            stringBuilder.append(maxReps).append(" Reps");
        }
        else if (minReps != -1 && maxSets == -1)
        {
            stringBuilder.append(minReps).append("+").append(" Reps");
        }
        else if (minReps != -1 && maxReps != -1)
        {
            stringBuilder.append(minReps).append("-").append(maxReps).append(" Reps");
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }



    private boolean setData(WorkoutExerciseModel workoutExerciseModel)
    {
        ArrayList<ExerciseLogModel> exerciseLogModelArrayList =(ArrayList<ExerciseLogModel>)  db.getExercisesLogsByWorkoutExerciseId(workoutExerciseModel.getWorkoutExerciseId());
        if (exerciseLogModelArrayList == null)
        {
            return false;
        }

        for (ExerciseLogModel logModel : exerciseLogModelArrayList)
        {
            String reps = Integer.toString(logModel.getRepsPerformed());
            String intensity = Double.toString(logModel.getIntensity());

            entries.add(new Entry(Float.valueOf(reps), Float.valueOf(intensity)));
        }

        Collections.sort(entries, new EntryXComparator());
        return true;
    }

    private void buildLineGraph(WorkoutExerciseModel workoutExerciseModel)
    {
        if (!setData(workoutExerciseModel))
        {
            entries = new ArrayList<>();
        }

        lineChart = findViewById(R.id.line_graph);
        lineChart.setScaleEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setDrawMarkers(false);

        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.enableDashedLine(10,8,15);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);

        lineChart.setVisibility(View.VISIBLE);
        lineChart.animateY(100);
        lineChart.setData(data);
        lineChart.invalidate();

        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(true);

        lineChart.setDrawBorders(true);
        lineChart.setPinchZoom(false);

        //Axes settings
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisRight = lineChart.getAxisRight();
        YAxis yAxisLeft = lineChart.getAxisLeft();

        xAxis.setGranularity(1f);
        yAxisLeft.setGranularity(5f);
        yAxisRight.setGranularity(5f);

        Description description = new Description();
        description.setText("Log an exercise to see data");
        lineChart.setDescription(description);
    }

}
