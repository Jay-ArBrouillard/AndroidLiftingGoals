package liftinggoals.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.liftinggoals.R;

public class WorkoutEditDialog extends AppCompatDialogFragment {
    private EditText minimumSets;
    private EditText minimumReps;
    private EditText maximumSets;
    private EditText maximumReps;
    private EditText weight;
    private WorkoutEditDialogListener listener;
    private int position;

    public WorkoutEditDialog(int position)
    {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.workout_edit_dialog, null);

        builder.setView(view)
                .setTitle("Exercise Details")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String minSets = minimumSets.getText().toString();
                        String maxSets = maximumSets.getText().toString();
                        String minReps = minimumReps.getText().toString();
                        String maxReps = maximumReps.getText().toString();
                        String strWeight = weight.getText().toString();
                        listener.editExerciseDetails(position, minSets, maxSets, minReps, maxReps, strWeight);
                    }
                });

        minimumSets = view.findViewById(R.id.workout_edit_dialog_min_sets);
        maximumSets = view.findViewById(R.id.workout_edit_dialog_max_sets);
        minimumReps = view.findViewById(R.id.workout_edit_dialog_min_reps);
        maximumReps = view.findViewById(R.id.workout_edit_dialog_max_reps);
        weight = view.findViewById(R.id.workout_edit_dialog_intensity);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (WorkoutEditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement WorkoutEditDialog");
        }
    }

    public interface WorkoutEditDialogListener
    {
        void editExerciseDetails(int position, String minSets, String maxSets, String minReps, String maxReps, String weight);
    }
}
