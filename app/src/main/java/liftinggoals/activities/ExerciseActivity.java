package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.liftinggoals.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import liftinggoals.classes.ExerciseLogModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.data.DatabaseHelper;

public class ExerciseActivity extends AppCompatActivity {
    private LineChart lineChart;
    private Button doneButton;
    private Button logButton;
    private ArrayList<WorkoutExerciseModel> exerciseList;
    private DatabaseHelper db;
    private double weight;
    private int reps;
    private ArrayList<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        exerciseList = getIntent().getExtras().getParcelableArrayList("exercise_list");
        final Spinner exerciseSpinner = findViewById(R.id.exercise_selection);
        db = new DatabaseHelper(this);
        db.openDB();

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
                exerciseLogModel.setSetPerformed(Integer.parseInt(set.getText().toString().trim()));
                EditText reps = findViewById(R.id.exercise_activity_reps_value);
                exerciseLogModel.setRepsPerformed(Integer.parseInt(reps.getText().toString().trim()));
                EditText weight = findViewById(R.id.exercise_activity_weight_value);
                exerciseLogModel.setIntensity(Double.parseDouble(weight.getText().toString()));

                //db.insertExerciseLog(exerciseLogModel);

                boolean found = false;
                for (int i = 0; i < entries.size(); i++)
                {
                    if (i < entries.size())
                    {
                        if (entries.get(i).getX() >= Float.parseFloat(reps.getText().toString()))
                        {
                            entries.add(i, new Entry(Float.parseFloat(reps.getText().toString()), Float.parseFloat(weight.getText().toString())));
                            found = true;
                            break;
                        }
                    }
                }

                if (!found)
                {
                    entries.add(new Entry(Float.parseFloat(reps.getText().toString()), Float.parseFloat(weight.getText().toString())));
                }


                initializeLineGraph();
            }
        });

        for (ExerciseLogModel model : db.getAllExerciseLogs())
        {
            System.out.println(model.getSetPerformed() + ": " + model.getRepsPerformed() + ": " + model.getIntensity());
        }



        ArrayList<WorkoutExerciseModel> list = exerciseList;
        ArrayList<String> exerciseNames = new ArrayList<>();
        for (WorkoutExerciseModel e : list)
        {
            exerciseNames.add(e.getExercise().getExerciseName());
        }
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(ExerciseActivity.this, android.R.layout.simple_list_item_1, exerciseNames);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);

        //this leaves the keyboard hidden on load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initializeLineGraph();
    }

    private void initializeLineGraph()
    {
        lineChart = findViewById(R.id.line_graph);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

//        entries.add(new Entry(0,945f));
//        entries.add(new Entry(1,1040f));
//        entries.add(new Entry(2,1133f));
//        entries.add(new Entry(3,1240f));
//        entries.add(new Entry(4,1369f));
//        entries.add(new Entry(5,1487f));
//        entries.add(new Entry(6,1501f));
//        entries.add(new Entry(7,1645f));
//        entries.add(new Entry(8,1578f));
//        entries.add(new Entry(9,1695f));


        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setFillAlpha(110);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(3f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);

        lineChart.setVisibility(View.VISIBLE);
        //lineChart.animateY(1000);
        lineChart.setData(data);
        lineChart.invalidate();

        Description description = new Description();
        description.setText("Log an exercise to see data");
        lineChart.setDescription(description);
    }
}
