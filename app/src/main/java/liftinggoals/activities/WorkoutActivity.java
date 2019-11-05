package liftinggoals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftinggoals.R;
import java.util.ArrayList;
import liftinggoals.adapters.WorkoutAdapter;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.WorkoutModel;
import liftinggoals.fragments.RoutineEditFragment;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class WorkoutActivity extends AppCompatActivity {
    private ArrayList<WorkoutModel> workoutsList;
    private ArrayList<String> workoutItems;
    private ArrayList<ExerciseModel> exerciseList;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //Get info from Bundle

        workoutsList = savedInstanceState.getParcelableArrayList("routine_item");
        //Set title of WorkoutFragment to the passed RoutineName from RoutineFragment
        TextView title = findViewById(R.id.fragment_multiple_workout_title);
        title.setText(savedInstanceState.getString("routine_name"));

        recyclerView = findViewById(R.id.multiple_workout_recycler_view);
        initializeRecyclerView();
        initializeActionSearch();
        initializeSwipe();

    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(this);

        adapter = new WorkoutAdapter(workoutsList);
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent startExerciseActivity = new Intent(WorkoutActivity.this, ExerciseActivity.class);
                startActivity(startExerciseActivity);
            }

            @Override
            public void onItemEdit(int position) {
                //Remove
                getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), new RoutineEditFragment()).commit();
            }

        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initializeActionSearch() {
        //Search
        search = findViewById(R.id.fragment_multiple_workout_action_search);
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
