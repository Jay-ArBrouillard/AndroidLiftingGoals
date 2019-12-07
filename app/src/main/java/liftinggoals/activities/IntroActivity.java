package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liftinggoals.R;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.adapters.IntroViewPagerAdapter;
import liftinggoals.models.ScreenItem;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabIndicator;
    private Button nextButton;
    private Button getStartedBtn;
    private int position;
    private Animation btnAnim;
    private TextView skipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (!restorePrefData())
        {
            Intent routineIntent = new Intent(IntroActivity.this, RoutineActivity.class);
            startActivity(routineIntent);
            finish();
        }

        StringBuilder routineDescription = new StringBuilder();
        routineDescription.append("\u2022 The routines page will be your hub that will contain all the workouts you have created.\n");
        routineDescription.append("\u2022 \"Create a routine\" will add an empty routine.\n");
        routineDescription.append("\u2022 Swipe a routine left or right to delete a routine\n");
        routineDescription.append("\u2022 Click on the edit pencil icon to edit a routine. ");

        StringBuilder routineEditDescription = new StringBuilder();
        routineEditDescription.append("\u2022 Items with the black \"edit\" pencil are editable items.\n");
        routineEditDescription.append("\u2022 After making changes to routines or workouts, the status check mark in the top right corner may turn red.\n");
        routineEditDescription.append("\u2022  When it is a red check mark and you don't see a loading animation, click the red check mark to save your changes.");

        StringBuilder workoutDescription = new StringBuilder();
        workoutDescription.append("\u2022 Click a routine to view its workouts.\n");
        workoutDescription.append("\u2022 \"Create a workout\" will add an empty workout.\n");
        workoutDescription.append("\u2022 Swipe a workout left or right to delete a routine\n");
        workoutDescription.append("\u2022 Click on the edit pencil icon to edit a workout. ");

        final List<ScreenItem> list = new ArrayList<>();
        list.add(new ScreenItem("Routines", routineDescription.toString(), R.drawable.ic_folder_blue_48dp));
        list.add(new ScreenItem("Workouts", workoutDescription.toString(), R.drawable.ic_dumbbell_blue_48dp));
        list.add(new ScreenItem("Editing", routineEditDescription.toString(), R.drawable.ic_edit_black_24dp));

        //Views
        screenPager = findViewById(R.id.intro_view_pager);
        tabIndicator = findViewById(R.id.intro_tab_layout);
        nextButton = findViewById(R.id.intro_next_button);
        getStartedBtn = findViewById(R.id.intro_get_started_btn);
        skipText = findViewById(R.id.intro_skip_tutorial);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.intro_button_animation);

        //Adapter
        introViewPagerAdapter = new IntroViewPagerAdapter(this, list);
        screenPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);

        //Event Handlers
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < list.size()-1)
                {
                    position++;
                    screenPager.setCurrentItem(position);
                    getStartedBtn.setVisibility(View.INVISIBLE);
                }

                if (position == list.size()-1) //Last item of list
                {
                    loadFinalScreen();
                }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() < list.size()-1)
                {
                    getStartedBtn.setVisibility(View.INVISIBLE);
                }

                if (tab.getPosition() == list.size()-1)
                {
                    loadFinalScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent routineIntent = new Intent(IntroActivity.this, RoutineActivity.class);
                startActivity(routineIntent);
                finish();
            }
        });

        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent routineIntent = new Intent(IntroActivity.this, RoutineActivity.class);
                startActivity(routineIntent);
                finish();
            }
        });

        saveSharedPrefs();
    }

    private boolean restorePrefData()
    {
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        return sp.getBoolean("isIntroOpened", false);
    }

    private void saveSharedPrefs()
    {
        SharedPreferences sp = getSharedPreferences("lifting_goals", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }

    private void loadFinalScreen()
    {
        getStartedBtn.setVisibility(View.VISIBLE);
        getStartedBtn.setAnimation(btnAnim);
        getStartedBtn.animate();
    }
}
