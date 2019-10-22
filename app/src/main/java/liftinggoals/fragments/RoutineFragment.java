package liftinggoals.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import liftinggoals.classes.ExerciseModel;
import com.example.liftinggoals.R;
import liftinggoals.adapters.RoutineAdapter;
import liftinggoals.classes.RoutineModel;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.classes.WorkoutModel;

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

        recyclerView = view.findViewById(R.id.routine_fragment_recycler_view);

        initializeRecyclerView();
        initializeActionSearch(view);
        initializeSwipe();

        return view;
    }


    private void initializeRecyclerView() {
        routineModels = new ArrayList<>();


        //Passing data to WorkoutFragment
        ArrayList<WorkoutModel> liftingModel = new ArrayList<>();  //Contains Workout Variants in Routine
        ArrayList<ExerciseModel> liftingExercises = new ArrayList<>();    //Contain exercises in a Workout
        liftingExercises.add(new ExerciseModel("Bench Press"));
        liftingExercises.add(new ExerciseModel("High Row"));
        liftingExercises.add(new ExerciseModel("Leg Press"));
        liftingModel.add(new WorkoutModel("Chest Day"));
        liftingModel.add(new WorkoutModel("Back Day"));
        liftingModel.add(new WorkoutModel("Leg Day"));
        routineModels.add(new RoutineModel("Full Body Routine", "Jack-of-all Trades Routine", liftingModel));

        ArrayList<WorkoutModel> runningModel = new ArrayList<>();
        ArrayList<ExerciseModel> runningWorkouts = new ArrayList<>();    //Contain exercises in a Workout
        runningWorkouts.add(new ExerciseModel("Marathon"));
        runningWorkouts.add(new ExerciseModel("Sprints"));
        runningWorkouts.add(new ExerciseModel("Stretching"));
        runningModel.add(new WorkoutModel("Jog around the park"));
        runningModel.add(new WorkoutModel("Track Sprints"));
        runningModel.add(new WorkoutModel("Intervals"));
        routineModels.add(new RoutineModel("General Running Routine", "Intermediate Running Routine", runningModel));

        //Lab 5
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.Brouillard.lab5", Context.MODE_PRIVATE);
        String username = sp.getString("username", "null username");
        ArrayList<WorkoutModel> test = new ArrayList<>();
        routineModels.add(new RoutineModel("Hi, " + username, "This card is here to display usage of SharedPreferences", test));
        //end lab5

        //End data

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RoutineAdapter(routineModels);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("routine_item", routineModels.get(position).getWorkouts());   //Passing List<WorkoutModel> to WorkoutFragment
                bundle.putString("routine_name", routineModels.get(position).getRoutineName());
                Fragment selectedFragment = new WorkoutFragment();
                selectedFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), selectedFragment).commit();
            }

            @Override
            public void onItemEdit(int position) {
                getActivity().getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), new RoutinesEditFolderFragment()).commit();
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
