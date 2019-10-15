package com.example.liftinggoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {
    private ArrayList<WorkoutModel> workoutsList;
    private ArrayList<String> workoutItems;
    private ArrayList<ExerciseModel> exerciseList;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_workout_routine, container, false);

        //Get info from Bundle
        workoutsList = getArguments().getParcelableArrayList("routine_item");
        //Set title of WorkoutFragment to the passed RoutineName from RoutineFragment
        TextView title = (TextView) view.findViewById(R.id.fragment_multiple_workout_title);
        title.setText(getArguments().getString("routine_name"));

        recyclerView = view.findViewById(R.id.multiple_workout_recycler_view);
        initializeRecyclerView();
        initializeActionSearch(view);
        initializeSwipe();

        return view;
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(getActivity());

        adapter = new WorkoutAdapter(workoutsList);
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent startExerciseActivity = new Intent(getActivity(), ExerciseActivity.class);
                startActivity(startExerciseActivity);
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
        search = view.findViewById(R.id.fragment_multiple_workout_action_search);
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
        //Make the entire text box clickable aswell
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.onActionViewExpanded();
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
