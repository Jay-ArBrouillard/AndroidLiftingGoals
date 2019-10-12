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
        final ArrayList<RoutineModel> routineModels = new ArrayList<>();
        routineModels.add(new RoutineModel("Stretching Routine", "Test Description"));
        routineModels.add(new RoutineModel("Cardio Routine", "Test Description"));
        routineModels.add(new RoutineModel("Lifting Routine", "Test Description"));
        routineModels.add(new RoutineModel("Biking Routine", "Test Description"));
        routineModels.add(new RoutineModel("Log Splitting Routine", "Test Description"));
        routineModels.add(new RoutineModel("Starting Strength", "Easy Beginner program"));
        routineModels.add(new RoutineModel("5x5", "Simple Beginner to Intermediate strength program"));
        routineModels.add(new RoutineModel("Wendlers", "Advanced strength program"));

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RoutineAdapter(routineModels);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                routineModels.get(position); //Unused

                Fragment selectedFragment = new WorkoutFragment();

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
                //Toast toast = Toast.makeText(getActivity(),adapter.getItem(pos).getText1() + " Deleted", Toast.LENGTH_SHORT);
                //toast.show();
                adapter.delete(viewHolder, pos);
            }
        }).attachToRecyclerView(recyclerView);
    }

}
