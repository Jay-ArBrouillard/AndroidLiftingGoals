package com.example.liftinggoals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoutineFragment extends Fragment {
    private ArrayList<RoutineModel> routineModels;
    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    private String username;
    public RoutineFragment() {
        //Empty constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_routines, container, false);

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        recyclerView = view.findViewById(R.id.routine_fragment_recycler_view);

        initializeRecyclerView();
        initializeActionSearch(view);
        initializeSwipe();

        return view;
    }

    private void initializeRecyclerView() {
        routineModels = new ArrayList<>();

        ArrayList<WorkoutModel> workoutModels = new ArrayList<>();  //Contains Workout Variants in Routine
        ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();    //Contain exercises in a Workout
        exerciseModels.add(new ExerciseModel("Bench Press"));
        workoutModels.add(new WorkoutModel("Chest Day"));
        workoutModels.add(new WorkoutModel("Back Day"));
        workoutModels.add(new WorkoutModel("Leg Day"));

        routineModels.add(new RoutineModel(R.drawable.ic_dumbbell_blue_48dp, "Chest Routine", "Simple Chest Routine", workoutModels));

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RoutineAdapter(routineModels);

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
    }

    private void initializeActionSearch(View view) {
        //Search
        search = view.findViewById(R.id.action_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void initializeSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; //drag and drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                adapter.delete(viewHolder, pos);
            }
        }).attachToRecyclerView(recyclerView);
    }

}
