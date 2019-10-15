package com.example.liftinggoals;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class ExerciseDetailsFragment extends Fragment {
    LineChart lineChart;
    ListView listView;
    Button button;

    public ExerciseDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_details, container, false);
        lineChart = view.findViewById(R.id.exercise_details_line_chart);
        listView = view.findViewById(R.id.exercise_details_list_view);
        createLineChart();
        createListView();

        button = view.findViewById(R.id.fragment_exercise_details_back_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void createListView() {
        ArrayList<String> exercises = new ArrayList<>();
        exercises.add("Bench Press: 50lbs x 5 of 10");
        exercises.add("Bench Press: 75lbs x 5 of 8");
        exercises.add("Bench Press: 100lbs x 3 of 6");
        exercises.add("Bench Press: 125lbs x 3 of 8");
        exercises.add("Bench Press: 150lbs x 3 of 5");
        exercises.add("Bench Press: 175lbs x 2 of 5");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, exercises);
        listView.setAdapter(arrayAdapter);
    }

    private void createLineChart() {

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
