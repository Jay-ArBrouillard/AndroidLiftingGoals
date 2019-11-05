package liftinggoals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import liftinggoals.adapters.RoutineAdapter;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.WorkoutModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.fragments.HistoryFragment;
import liftinggoals.fragments.MapsFragment;
import liftinggoals.fragments.ProgressFragment;
import liftinggoals.fragments.RoutineFragment;
import liftinggoals.fragments.RoutinesEditFolderFragment;
import liftinggoals.fragments.SettingFragment;
import liftinggoals.fragments.WorkoutFragment;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class RoutineActivity extends AppCompatActivity {
    public ArrayList<RoutineModel> routineModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        recyclerView = findViewById(R.id.routine_fragment_recycler_view);

        initializeRecyclerView();
        initializeActionSearch();
        initializeSwipe();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_routine_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedFragment = new RoutineFragment();
                    break;
                case R.id.nav_progress:
                    selectedFragment = new ProgressFragment();
                    break;
                case R.id.nav_history:
                    selectedFragment = new HistoryFragment();
                    break;
                case R.id.nav_maps:
                    selectedFragment = new MapsFragment();
                    break;
                case R.id.nav_settings:
                    selectedFragment = new SettingFragment();
                    break;

            }
            if (selectedFragment != null)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
            }

            return true;    //Means we want to select the clicked item
        }
    };

    private void initializeRecyclerView() {
        fetchData();
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(this);
        adapter = new RoutineAdapter(routineModels);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("routine_item", routineModels.get(position).getWorkouts());   //Passing List<WorkoutModel> to WorkoutFragment
                bundle.putString("routine_name", routineModels.get(position).getRoutineName());
                Fragment selectedFragment = new WorkoutFragment();
                selectedFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();

                Intent selectWorkFromRoutine = new Intent(RoutineActivity.this, WorkoutActivity.class);
                startActivity(selectWorkFromRoutine);
            }

            @Override
            public void onItemEdit(int position) {
                //to change
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new RoutinesEditFolderFragment()).addToBackStack(null).commit();
            }

        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchData()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://3.221.56.60/initializeRoutines.php";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (Request.Method.GET, url, null, new
                Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        try
                        {
                            System.out.println("response length: " + response.length());
                            ArrayList<WorkoutModel> listOfWorkouts = new ArrayList<>(); //Ex: Workout A and Workout B
                            //
                            ArrayList<ExerciseModel> listOfExercises = new ArrayList<>(); //Ex: Workout A contains Squat(rep,set data), Bench, Row
                            //
                            String routineName = null;
                            String routineDesc = null;
                            int i = 0;
                            while (i < response.length())
                            {
                                JSONObject routineObj = response.getJSONObject(i);
                                routineName = routineObj.getString("routine_name");
                                routineDesc = routineObj.getString("description");
                                int totalWorkouts = routineObj.getInt("number_workouts");

                                int iterations = 0;
                                for (int j = 0 ; j < totalWorkouts; j++)
                                {
                                    i++;
                                    JSONObject nextObj = response.getJSONObject(i);
                                    listOfWorkouts.add(new WorkoutModel(nextObj.getString("workout_name"), nextObj.getString("description"), nextObj.getDouble("duration")));
                                    int totalExercises = nextObj.getInt("number_exercises");
                                    listOfExercises.clear();
                                    for (int k = 0; k < totalExercises*2; k++)
                                    {
                                        i++;
                                        JSONObject exerciseObj = response.getJSONObject(i);
                                        //Sets Reps
                                        if (exerciseObj.has("minimum_sets"))
                                        {
                                            //todo
                                        }

                                        //Exercise Name
                                        if (exerciseObj.has("exercise_name"))
                                        listOfExercises.add(new ExerciseModel(exerciseObj.getString("exercise_name")));
                                    }

                                    listOfWorkouts.get(listOfWorkouts.size()-1).setExercises(listOfExercises);
                                }
                                i++;
                            }   //End outer for loop

                            routineModels.add(new RoutineModel(routineName, routineDesc, listOfWorkouts));

                            //Testing Return value
                            for (RoutineModel r : routineModels)
                            {
                                System.out.println(r.getRoutineName() + ": " + r.getRoutineDescription());
                                for (WorkoutModel w : r.getWorkouts())
                                {
                                    System.out.println(w.getWorkoutName() + ": " + w.getEstimatedDuration());
                                    for (ExerciseModel e : w.getExercises())
                                    {
                                        System.out.println(e.getExerciseName());
                                    }
                                }
                            }
                            //EndTesting

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsObjRequest);
    }

    private void initializeActionSearch() {
        //Search
        search = findViewById(R.id.action_search);
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


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("routineModels", routineModels);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            routineModels = savedInstanceState.getParcelableArrayList("routineModels");
        }
    }

}
