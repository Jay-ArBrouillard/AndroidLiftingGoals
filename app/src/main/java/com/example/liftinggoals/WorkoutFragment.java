package com.example.liftinggoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        ArrayList<String> daysOfTheWeek = new ArrayList<>();
        daysOfTheWeek.add("Monday");
        daysOfTheWeek.add("Wednesday");
        daysOfTheWeek.add("Friday");

        listView = (ListView) view.findViewById(R.id.workout_listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, daysOfTheWeek);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent openExercises = new Intent(view.getContext(), ExerciseActivity.class);
                    startActivity(openExercises);
                }
            }
        });

        return view;
    }
}
