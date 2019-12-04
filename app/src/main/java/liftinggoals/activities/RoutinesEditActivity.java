package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.liftinggoals.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import liftinggoals.models.RoutineModel;
import liftinggoals.data.DatabaseHelper;
import liftinggoals.services.RoutineService;

public class RoutinesEditActivity extends AppCompatActivity {
    private ArrayList<RoutineModel> routineModels;
    private int selectedRoutineIndex;
    private EditText routineNameEditText;
    private EditText routineDescEditText;
    private DatabaseHelper db;
    private ImageView commitButton;
    private String routineNameUnchanged;
    private String routineDescUnchanged;
    private ResponseReceiver myReceiver;
    private LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_edit);
        routineModels = getIntent().getExtras().getParcelableArrayList("routine_models");
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        selectedRoutineIndex = sp.getInt("selected_routine_index", -1);
        db = new DatabaseHelper(this);
        db.openDB();

        loadingAnim = findViewById(R.id.routine_edit_loading_animation);
        loadingAnim.setVisibility(View.INVISIBLE);
        routineNameEditText = findViewById(R.id.activity_routines_edit_text);
        routineNameEditText.setText(routineModels.get(selectedRoutineIndex).getRoutineName());
        commitButton = findViewById(R.id.routine_activity_commit_button);
        commitButton.setClickable(false);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadingAnim.isAnimating())
                {
                    loadingAnim.setVisibility(View.VISIBLE);
                    loadingAnim.playAnimation();
                    commitButton.setImageResource(R.drawable.ic_checked_red_48dp);
                }
                Intent intent = new Intent(RoutinesEditActivity.this, RoutineService.class);
                intent.putExtra("type", "update");
                intent.putExtra("routineId", routineModels.get(selectedRoutineIndex).getRoutineId());
                intent.putExtra("routineName", routineNameEditText.getText().toString());
                intent.putExtra("description", routineDescEditText.getText().toString());
                startService(intent);
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //Quit the animation if it takes longer than 10 seconds
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingAnim.isAnimating())
                                {
                                    loadingAnim.cancelAnimation();
                                    loadingAnim.setVisibility(View.INVISIBLE);
                                    commitButton.setImageResource(R.drawable.ic_checked_green_48dp);
                                }
                            }
                        });

                        timer.cancel();
                    }
                }, 10000);
            }
        });

        routineNameUnchanged = routineNameEditText.getText().toString();
        routineNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(routineNameUnchanged))
                {
                    commitButton.setImageResource(R.drawable.ic_checked_red_48dp);
                    commitButton.setClickable(true);
                }
                else
                {
                    commitButton.setImageResource(R.drawable.ic_checked_neutral_48dp);
                    commitButton.setClickable(false);
                }
            }
        });

        routineDescEditText = findViewById(R.id.activity_routines_edit_description_text);
        routineDescEditText.setText(routineModels.get(selectedRoutineIndex).getRoutineDescription());
        routineDescUnchanged = routineModels.get(selectedRoutineIndex).getRoutineDescription();
        routineDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals(routineDescUnchanged))
                {
                    commitButton.setImageResource(R.drawable.ic_checked_red_48dp);
                    commitButton.setClickable(true);
                }
                else
                {
                    commitButton.setImageResource(R.drawable.ic_checked_neutral_48dp);
                    commitButton.setClickable(false);
                }
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        BottomNavigationView bottomNavigation = findViewById(R.id.activity_routines_edit_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent selectedActivity = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_routine:
                    selectedActivity = new Intent(RoutinesEditActivity.this, RoutineActivity.class);
                    break;
                case R.id.nav_progress:
                    selectedActivity = new Intent(RoutinesEditActivity.this, ProgressActivity.class);
                    break;
                case R.id.nav_history:
                    selectedActivity = new Intent(RoutinesEditActivity.this, HistoryActivity.class);
                    break;
                case R.id.nav_maps:
                    selectedActivity = new Intent(RoutinesEditActivity.this, MapsActivity.class);
                    break;
                case R.id.nav_settings:
                    selectedActivity = new Intent(RoutinesEditActivity.this, SettingsActivity.class);
                    break;

            }
            if (selectedActivity != null)
            {
                startActivity(selectedActivity);
            }

            return true;    //Means we want to select the clicked item
        }
    };

    public class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("routineAction"))
            {
                loadingAnim.cancelAnimation();
                loadingAnim.setVisibility(View.INVISIBLE);
                commitButton.setImageResource(R.drawable.ic_checked_green_48dp);
            }
        }
    }

    private void setReceiver() {
        myReceiver = new RoutinesEditActivity.ResponseReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("routineAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        setReceiver();
        super.onStart();
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
