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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {
    private ArrayList<WorkoutModel> workoutsList;
    private ArrayList<String> workoutItems;
    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_workout_routine, container, false);

        //Get info from Bundle
        if (getArguments() != null) {
            workoutsList = getArguments().getParcelableArrayList("routine_item");
            workoutItems = new ArrayList<>();
            for (WorkoutModel workoutModel : workoutsList) {
                workoutItems.add(workoutModel.getWorkoutName());
            }
        }

        recyclerView = view.findViewById(R.id.multiple_workout_list_view);
        initializeRecyclerView();
/*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, workoutItems);
        recyclerView.setAdapter(arrayAdapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent openExercises = new Intent(view.getContext(), ExerciseActivity.class);
                    startActivity(openExercises);
                }
            }
        });
*/
        return view;
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(getActivity());
        /*
        adapter = new RoutineAdapter(workoutItems);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("routine_item", routineModels.get(position).getWorkouts());
                Fragment selectedFragment = new WorkoutFragment();
                selectedFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), selectedFragment).commit();
            }

            @Override
            public void onItemEdit(int position) {
                //unimplemented
            }

        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        */

    }
}
