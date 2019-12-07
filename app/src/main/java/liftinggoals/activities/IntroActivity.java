package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.liftinggoals.R;
import com.google.android.material.tabs.TabLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (restorePrefData())
        {
            Intent routineIntent = new Intent(IntroActivity.this, RoutineActivity.class);
            startActivity(routineIntent);
            finish();
        }

        final List<ScreenItem> list = new ArrayList<>();
        list.add(new ScreenItem("Welcome to Lifting Goals", "The Quick Brown Fox jumped over the lazy dog", R.drawable.ic_instruction_496_dp));
        list.add(new ScreenItem("Boom", "The Quick Brown Fox jumped over the lazy dog", R.drawable.ic_instruction_496_dp));
        list.add(new ScreenItem("Pow", "The Quick Brown Fox jumped over the lazy dog", R.drawable.ic_instruction_496_dp));

        //Views
        screenPager = findViewById(R.id.intro_view_pager);
        tabIndicator = findViewById(R.id.intro_tab_layout);
        nextButton = findViewById(R.id.intro_next_button);
        getStartedBtn = findViewById(R.id.intro_get_started_btn);
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
                    //Todo : show the get started button and hide the indicator and the next button

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

        getStartedBtn.setAnimation(btnAnim);
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
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
        getStartedBtn.animate();

    }
}
