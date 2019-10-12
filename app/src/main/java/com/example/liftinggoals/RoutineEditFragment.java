package com.example.liftinggoals;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RoutineEditFragment extends Fragment {


    public RoutineEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_routines, container, false);

        RoutineEditFragment startingFragment = new RoutineEditFragment();
//        startingFragment.setArguments(bundle);


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, startingFragment).commit();

        return view;
    }

}
