package com.example.liftinggoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        ArrayList<String> exercises = new ArrayList<>();
        exercises.add("Squat");
        exercises.add("Bench Press");
        exercises.add("Barbell Row");
        exercises.add("Deadlift");
        exercises.add("Shoulder Press");
        exercises.add("Hang Clean");

        listView = (ListView) view.findViewById(R.id.progress_list_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, exercises);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment selectedFragment = null;

                if (position == 1) {
                    selectedFragment = new ExerciseDetailsFragment();
                }


                getActivity().getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), selectedFragment).commit();
            }
        });

        return view;
    }
}
