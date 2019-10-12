package com.example.liftinggoals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = view.findViewById(R.id.history_list_view);
        createListView();

        return view;
    }

    private void createListView() {
        ArrayList<String> exercises = new ArrayList<>();
        exercises.add("Chest: Bench Press, DB Bench, Machine Fly");
        exercises.add("Legs: Squat, DeadLift, Hamstring curl");
        exercises.add("Abs: Seated Crunch, Leg lifts, Roll outs");
        exercises.add("Shoulders: Overhead press, Clean and Jerk, Arnold Press");
        exercises.add("Arms: EZ Bar Curl, Tricep Extensions, Pushups");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, exercises);
        listView.setAdapter(arrayAdapter);
    }
}
