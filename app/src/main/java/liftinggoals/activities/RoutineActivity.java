package liftinggoals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

import liftinggoals.adapters.RoutineAdapter;
import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.RoutineWorkoutModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.classes.WorkoutModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;

public class RoutineActivity extends AppCompatActivity {
    public ArrayList<RoutineModel> routineModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        recyclerView = findViewById(R.id.routine_fragment_recycler_view);

        db = new DatabaseHelper(getApplicationContext());
        db.openDB();
        initializeRecyclerView();
        initializeActionSearch();
        initializeSwipe();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_routines_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    //already on this page so don't do anything
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(RoutineActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(RoutineActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(RoutineActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(RoutineActivity.this, SettingsActivity.class);
                    break;

            }
            if (selectedActivity != null)
            {
                startActivity(selectedActivity);
            }

            return true;    //Means we want to select the clicked item
        }
    };

    private void initializeRecyclerView() {
        fetchRemoteData();

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));

        layoutManager = new LinearLayoutManager(this);
        adapter = new RoutineAdapter(routineModels);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Fragment selectedFragment = new WorkoutFragment();
//                selectedFragment.setArguments(bundle);
//
//                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();

                Intent selectWorkFromRoutine = new Intent(RoutineActivity.this, WorkoutActivity.class);
                selectWorkFromRoutine.putParcelableArrayListExtra("routine_item", routineModels.get(position).getWorkouts());
                selectWorkFromRoutine.putExtra("routine_name", routineModels.get(position).getRoutineName());
                /*
                Bundle extras = new Bundle();
                extras.putParcelableArrayList("routine_item", routineModels.get(position).getWorkouts());                    //Passing List<WorkoutModel> to WorkoutFragment
                extras.putString("routine_name", routineModels.get(position).getRoutineName());
                selectWorkFromRoutine.putExtras(extras);*/
                startActivity(selectWorkFromRoutine);
            }

            @Override
            public void onItemEdit(int position) {
                Intent editRoutineActivity = new Intent(RoutineActivity.this, RoutinesEditActivity.class);
                editRoutineActivity.putExtra("routine_name", routineModels.get(position).getRoutineName());
                startActivity(editRoutineActivity);
            }

        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRemoteData()
    {
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.openDB();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://3.221.56.60/initializeRoutines.php";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (Request.Method.GET, url, null, new
                Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        try
                        {
                            System.out.println("response length: " + response.length());
                            ArrayList<WorkoutModel> listOfWorkouts = new ArrayList<>(); //Ex: Workout A and Workout B
                            ArrayList<WorkoutExerciseModel> listOfExercises = new ArrayList<>(); //Ex: Workout A contains Squat(rep,set data), Bench, Row
                            String routineName = null;
                            String routineDesc = null;
                            int i = 0;
                            while (i < response.length())
                            {
                                JSONObject routineObj = response.getJSONObject(i);
                                routineName = routineObj.getString("routine_name");
                                routineDesc = routineObj.getString("description");
                                int totalWorkouts = routineObj.getInt("number_workouts");

                                if (db.getRoutine(routineName) == null)
                                db.insertRoutine(routineName, routineDesc, totalWorkouts);

                                for (int j = 0 ; j < totalWorkouts; j++)
                                {
                                    i++;
                                    JSONObject routineWorkoutObj = response.getJSONObject(i);

                                    int routineId = routineWorkoutObj.getInt("routine_id");
                                    int workoutId = routineWorkoutObj.getInt("workout_id");
                                    if (routineWorkoutObj.has("routine_workout_id") && db.getRoutineWorkout(routineId,workoutId) == null)
                                        db.insertRoutineWorkout(routineId, workoutId);

                                    i++;
                                    JSONObject nextObj = response.getJSONObject(i);
                                    listOfWorkouts.add(new WorkoutModel(nextObj.getString("workout_name"), nextObj.getString("description"), nextObj.getDouble("duration")));
                                    int totalExercises = nextObj.getInt("number_exercises");

                                    if (db.getWorkout(nextObj.getInt("workout_id")) == null)
                                        db.insertWorkout(nextObj.getString("workout_name"), nextObj.getString("description"), nextObj.getDouble("duration"), totalExercises);

                                    listOfExercises.clear();
                                    WorkoutExerciseModel workoutExerciseModel = new WorkoutExerciseModel();
                                    for (int k = 0; k < totalExercises*2; k++)
                                    {
                                        i++;
                                        JSONObject exerciseObj = response.getJSONObject(i);
                                        if (k % 2 == 0)
                                        {
                                            String strMinSets = exerciseObj.getString("minimum_sets");
                                            String strMinReps = exerciseObj.getString("minimum_reps");
                                            String strMaxSets = exerciseObj.getString("maximum_sets");
                                            String strMaxReps = exerciseObj.getString("maximum_reps");
                                            //Quick null fix
                                            if (strMinSets == "null")
                                                strMinSets = "-1";
                                            if (strMinReps == "null")
                                                strMinReps = "-1";
                                            if (strMaxSets == "null")
                                                strMaxSets = "-1";
                                            if (strMaxReps == "null")
                                                strMaxReps = "-1";
                                            //Fix later
                                            workoutExerciseModel.setMinimumReps(Integer.parseInt(strMinSets));
                                            workoutExerciseModel.setMinimumReps(Integer.parseInt(strMinReps));
                                            workoutExerciseModel.setMaximumSets(Integer.parseInt(strMaxSets));
                                            workoutExerciseModel.setMaximumReps(Integer.parseInt(strMaxReps));

                                            if (db.getWorkoutExercise(exerciseObj.getInt("workout_exercise_id")) == null)
                                            {
                                                db.insertWorkoutExercise(exerciseObj.getInt("workout_id"), exerciseObj.getInt("exercise_id"),
                                                        Integer.parseInt(strMinSets), Integer.parseInt(strMinReps), Integer.parseInt(strMaxSets), Integer.parseInt(strMaxReps));
                                            }
                                        }
                                        else
                                        {
                                            String exerciseName = exerciseObj.getString("exercise_name");
                                            //Create ExerciseModel for Workout
                                            ExerciseModel exerciseModel = new ExerciseModel(exerciseName);
                                            workoutExerciseModel.setExercise(exerciseModel);
                                            listOfExercises.add(workoutExerciseModel);
                                            workoutExerciseModel = new WorkoutExerciseModel(); //Clear exercise model
                                        }
                                    }

                                    listOfWorkouts.get(listOfWorkouts.size()-1).setExercises(listOfExercises);
                                }
                                i++;
                            }   //End outer for loop

                            routineModels.add(new RoutineModel(routineName, routineDesc, listOfWorkouts));

                            //Check local database
                            for (RoutineModel r : db.getAllRoutines())
                            {
                                System.out.println(r.getRoutineName() + ": " + r.getRoutineDescription());
                            }

                            for (RoutineWorkoutModel rWM : db.getAllRoutineWorkouts())
                            {
                                System.out.println(rWM.getRoutineId() + ": " + rWM.getWorkoutId());
                            }


                            for (WorkoutModel w : db.getAllWorkouts())
                            {
                                System.out.println(w.getWorkoutName() + ": " + w.getDescription() + ": " + w.getEstimatedDuration() + ": " + w.getNumberExercises());
                            }

                            for (WorkoutExerciseModel wEM : db.getAllWorkoutExercises())
                            {
                                System.out.println(wEM.getWorkoutId() + ": " + wEM.getExerciseId() + ": " + wEM.getMinimumSets() + ": " + wEM.getMinimumReps() + ": " + wEM.getMaximumSets() + ": " + wEM.getMaximumSets());
                            }




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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }
}
