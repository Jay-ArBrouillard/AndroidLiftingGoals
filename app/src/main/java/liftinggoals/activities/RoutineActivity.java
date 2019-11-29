package liftinggoals.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import liftinggoals.services.DefaultRoutineService;
import liftinggoals.adapters.RoutineAdapter;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.RoutineWorkoutModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.dialogs.DefaultRoutineDialog;
import liftinggoals.misc.RoutineModelHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.services.InitializeRoutineService;

public class RoutineActivity extends AppCompatActivity {
    private ArrayList<RoutineModel> routineModels;
    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;
    private DatabaseHelper db;
    private ResponseReceiver myReceiver;
    private String username;
    private int userId;
    private boolean isFirstLogin;
    private Button fetchButton;
    private LoadToast defaultLoadToast;
    private LoadToast initializeLoadToast;
    private CardView createRoutine;
    private boolean serviceError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        routineModels = new ArrayList<>();

        recyclerView = findViewById(R.id.routine_fragment_recycler_view);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));//only do this once

        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        userId = sp.getInt("UserId", -1);
        username = sp.getString("username", null);
        isFirstLogin = sp.getBoolean("firstLogin", false);
        defaultLoadToast = new LoadToast(this);
        initializeLoadToast = new LoadToast(this);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        db = new DatabaseHelper(getApplicationContext());
        db.openDB();

        initializeActionSearch();
        initializeSwipe();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_routines_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        fetchButton = findViewById(R.id.fetch_remote_data);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultLoadToast.setText("Fetching default routines...");
                defaultLoadToast.setTranslationY(displayMetrics.heightPixels/2);
                defaultLoadToast.setBorderWidthDp(100);
                DefaultRoutineDialog dialog = new DefaultRoutineDialog(username, defaultLoadToast);
                dialog.show(getSupportFragmentManager(), "Routine Dialog");
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //Quit the defaultLoadToast if it takes longer than 10 seconds
                        if (serviceError)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    defaultLoadToast.error();
                                }
                            });
                        }
                        timer.cancel();
                    }
                }, 10000);
            }
        });

        //If it is the first Login Start service to get default routines
        if (isFirstLogin)
        {
            Intent defaultRoutine = new Intent(RoutineActivity.this, DefaultRoutineService.class);
            defaultRoutine.putExtra("username", username);
            startService(defaultRoutine);
        }

        final ImageView commitChangesImage = findViewById(R.id.routine_activity_commit_changes);

        createRoutine = findViewById(R.id.activity_routine_create_a_workout);
        createRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertRoutine(userId, "Untitled Routine", "Description", 0);
                routineModels.add(new RoutineModel(userId, "Untitled Routine", "Empty Description"));
                adapter.notifyItemInserted(routineModels.size()-1);
                commitChangesImage.setImageResource(R.drawable.ic_checked_green_48dp);
            }
        });

        //Fetch initialize Routines
        Intent intent = new Intent(RoutineActivity.this, InitializeRoutineService.class);
        intent.putExtra("user_id", userId);
        initializeLoadToast.setText("Loading your routines...");
        initializeLoadToast.setTranslationY(displayMetrics.heightPixels/2);
        initializeLoadToast.setBorderWidthDp(100);
        initializeLoadToast.show();
        startService(intent);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initializeLoadToast.error();
                    }
                });
                timer.cancel();
            }
        }, 20000);
    }

    private void setReceiver() {
        myReceiver = new ResponseReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action");
        intentFilter.addAction("initializeRoutinesAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            serviceError = false;

            if (intent.getAction().equals("initializeRoutinesAction"))
            {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
                initializeRecyclerView();
                initializeActionSearch();
                initializeSwipe();
                initializeLoadToast.success();
            }
            else if (intent.getAction().equals("action"))
            {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
                initializeRecyclerView();
                initializeActionSearch();
                initializeSwipe();
                defaultLoadToast.success();
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
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

    private void initializeRecyclerView()
    {
        routineModels =  RoutineModelHelper.populateRoutineModels(this, userId);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RoutineAdapter(routineModels);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position)
            {
                Intent selectWorkFromRoutine = new Intent(RoutineActivity.this, WorkoutActivity.class);
                selectWorkFromRoutine.putParcelableArrayListExtra("routine_models", routineModels);
                SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
                sp.edit().putInt("selected_routine_index", position).commit();
                startActivity(selectWorkFromRoutine);
            }

            @Override
            public void onItemEdit(int position) {
                Intent editRoutineActivity = new Intent(RoutineActivity.this, RoutinesEditActivity.class);
                editRoutineActivity.putParcelableArrayListExtra("routine_models", routineModels);
                SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
                sp.edit().putInt("selected_routine_index", position).commit();
                startActivity(editRoutineActivity);
            }

        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    // Print Helper
    private void printLocalDatabase()
    {

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

        for (ExerciseModel e : db.getAllExercises())
        {
            System.out.println(e.getExerciseName());
        }
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
    protected void onStart() {
        setReceiver();
        super.onStart();
        initializeRecyclerView();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }

}
