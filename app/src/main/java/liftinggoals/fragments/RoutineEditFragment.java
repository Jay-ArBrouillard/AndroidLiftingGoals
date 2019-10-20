package liftinggoals.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liftinggoals.R;

public class RoutineEditFragment extends Fragment {
    private RecyclerView recyclerView;

    public RoutineEditFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_edit, container, false);

        recyclerView = view.findViewById(R.id.edit_routine_recycler_view);
        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView()
    {
        //Todo

    }

}
