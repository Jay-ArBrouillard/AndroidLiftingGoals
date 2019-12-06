package liftinggoals.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import liftinggoals.dialogs.DeleteRoutineDialog;
import liftinggoals.services.DefaultRoutineService;
import liftinggoals.adapters.RoutineAdapter;
import liftinggoals.models.RoutineModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.dialogs.DefaultRoutineDialog;
import liftinggoals.misc.RoutineModelHelper;
import liftinggoals.misc.VerticalSpaceItemDecoration;
import liftinggoals.services.InitializeRoutineService;
import liftinggoals.services.RoutineService;

public class RoutineActivity extends AppCompatActivity implements DeleteRoutineDialog.DeleteDialogListener {
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
    private CardView createRoutine;
    private ImageView commitChangesImage;
    private LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        routineModels = new ArrayList<>();

        loadingAnim = findViewById(R.id.routine_loading_animation);
        commitChangesImage = findViewById(R.id.routine_activity_commit_changes);
        recyclerView = findViewById(R.id.routine_fragment_recycler_view);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(4));//only do this once

        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        userId = sp.getInt("UserId", -1);
        username = sp.getString("username", null);
        isFirstLogin = sp.getBoolean("firstLogin", false);

        if (db == null)
        {
            db = new DatabaseHelper(getApplicationContext());
            db.openDB();
        }

        initializeActionSearch();
        initializeSwipe();

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_routines_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        fetchButton = findViewById(R.id.fetch_remote_data);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultRoutineDialog dialog = new DefaultRoutineDialog(username, loadingAnim);
                dialog.show(getSupportFragmentManager(), "Routine Dialog");
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //Quit the animation if it takes longer than 10 seconds
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnim.cancelAnimation();
                                loadingAnim.setVisibility(View.INVISIBLE);
                                commitChangesImage.setImageResource(R.drawable.ic_checked_red_48dp);
                            }
                        });

                        timer.cancel();
                    }
                }, 10000);
            }
        });

        createRoutine = findViewById(R.id.activity_routine_create_a_workout);
        createRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!loadingAnim.isAnimating())
                {
                    loadingAnim.setVisibility(View.VISIBLE);
                    loadingAnim.playAnimation();
                    commitChangesImage.setImageResource(R.drawable.ic_checked_red_48dp);
                }
                Intent intent = new Intent(RoutineActivity.this, RoutineService.class);
                intent.putExtra("type", "insert");
                intent.putExtra("userId", userId);
                startService(intent);
            }
        });

        //Fetch initialize Routines
        Intent intent = new Intent(RoutineActivity.this, InitializeRoutineService.class);
        intent.putExtra("user_id", userId);
        startService(intent);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingAnim.cancelAnimation();
                    }
                });
                timer.cancel();
            }
        }, 20000);

        //Login anim
        loadingAnim.cancelAnimation();
        loadingAnim.setVisibility(View.INVISIBLE);
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
                openDeleteDialog(pos, adapter.getItem(pos));
                adapter.delete(viewHolder, pos);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void openDeleteDialog(int position, Object item)
    {
        DeleteRoutineDialog deleteRoutineDialog = new DeleteRoutineDialog(position, item);
        deleteRoutineDialog.show(getSupportFragmentManager(), "deleteRoutineDialog");
    }

    @Override
    public void onCancelClicked(int position, Object item)
    {
        routineModels.add(position, ((RoutineModel)item));
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onDeleteClicked(int position, Object item)
    {
        if (!loadingAnim.isAnimating())
        {
            loadingAnim.setVisibility(View.VISIBLE);
            loadingAnim.playAnimation();
        }
        commitChangesImage.setImageResource(R.drawable.ic_checked_red_48dp);
        Intent intent = new Intent(RoutineActivity.this, RoutineService.class);
        intent.putExtra("type", "delete");
        intent.putExtra("routineId", ((RoutineModel)item).getRoutineId());
        intent.putExtra("userId", userId);
        intent.putExtra("routineModels", routineModels);
        startService(intent);
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

    private void setReceiver() {
        myReceiver = new ResponseReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("defaultRoutineAction");
        intentFilter.addAction("errorDefaultRoutine");
        intentFilter.addAction("initializeRoutinesAction");
        intentFilter.addAction("routineAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("initializeRoutinesAction"))
            {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
                initializeRecyclerView();
                initializeActionSearch();
                initializeSwipe();
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesImage.setImageResource(R.drawable.ic_checked_green_48dp);
            }
            else if (intent.getAction().equals("defaultRoutineAction"))
            {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
//                initializeRecyclerView();
//                initializeActionSearch();
//                initializeSwipe();
//                loadingAnim.cancelAnimation();
//                loadingAnim.setVisibility(View.INVISIBLE);
//                commitChangesImage.setImageResource(R.drawable.ic_checked_green_48dp);
                Intent thisIntent = getIntent();
                finish();
                startActivity(thisIntent);
            }
            else if (intent.getAction().equals("errorDefaultRoutine"))
            {
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesImage.setImageResource(R.drawable.ic_checked_red_48dp);
            }
            else if (intent.getAction().equals("routineAction"))
            {
                initializeRecyclerView();
                initializeActionSearch();
                initializeSwipe();
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitChangesImage.setImageResource(R.drawable.ic_checked_green_48dp);
            }
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
